package com.farmtec.mcc.service;

import com.farmtec.amqp.client.dto.MccEventAmqpDto;
import com.farmtec.amqp.client.producer.SendAmqpMessage;
import com.farmtec.io.handler.BasicHandler;
import com.farmtec.io.message.Message;
import com.farmtec.mcc.cdr.dto.Cdr;
import com.farmtec.mcc.cdr.event.CdrEventPublisher;
import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.stats.ServiceIOMcuStats;
import com.farmtec.mcc.stats.ServiceIOStats;
import com.farmtec.mcc.utils.Util;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class ServiceIoReceiver extends BasicHandler {
    Logger logger = LoggerFactory.getLogger(ServiceIoReceiver.class);

    @Autowired
    MccService mccService;
    @Autowired
    AdcService adcService;
    @Autowired
    TimerService timerService;
    @Autowired
    PortnService portnService;

    @Autowired
    ServiceIOStats serviceIOStats;

    @Autowired
    CdrEventPublisher cdrEventPublisher;

    @Autowired
    SendAmqpMessage sendAmqpMessage;

    @Override
    public void execute(Message msg) {

        Map<String,Integer> decodedMessage=msg.decodeToMap();
        if (logger.isInfoEnabled())
            logger.info("Processing message "+ msg.toString());
        // this is the MCU sending its report Update
        long start=System.currentTimeMillis();
        if(Operation.ReportUpdate.getValue()==(byte)( decodedMessage.get("operation")&0xFF)){
            logger.info("Processing ReportUpdate for MCU Addr "+ Util.IntegerToByteReadableHex(decodedMessage.get("address")) );
            long a=System.currentTimeMillis();
            Atmega mcu=mccService.getMcuDetailsByAddress(Util.IntegerToByteReadableHex(decodedMessage.get("address")));
            long b=System.currentTimeMillis();
            long x=b-a;
            logger.info("MCU Addr["+Util.IntegerToByteReadableHex(decodedMessage.get("address"))+"] GET took ["+x+"]ms");
            if(null!=mcu){
                /*
                search for timerX X= 0 1 2 A
                timer0 timer1 timer2
                 */
                Date now=new Date();
                List<Timer> timers=mcu.getTimers();
                for (Timer timer:timers) {
                    logger.info("looping timer "+timer.getName());
                    if(decodedMessage.containsKey(timer.getName())){

                        logger.info("Found timer " + timer.getName() + "in payload...going to update");

                        if(logger.isDebugEnabled()) {
                            logger.debug("Found timer " + timer.getName() + "in payload...going to update with value [" + decodedMessage.get(timer.getName()) + "]");
                        }
                        TimerDto timerDto=new TimerDto();
                        timerDto.setId(timer.getId());
                        timerDto.setOutPutCompareRegister(0xFF&decodedMessage.get(timer.getName()));
                        timerService.updateTimerById(timerDto);

                        //Generate Cdr Object
                        Cdr cdr=new Cdr();
                        cdr.setAddr(mcu.getAddress());
                        cdr.setOperation(Operation.ReportUpdate.getValue());
                        cdr.setNow(now);
                        cdr.setData("TIMER,"+timer.getName()+","+timerDto.getId()+","+timerDto.getOutPutCompareRegister()+","+timer.getMode().getValue());
                        //publish event
                        cdrEventPublisher.generateCdr(this,cdr);

                        //generate Event AMQP
                        MccEventAmqpDto mccEventAmqpDto=new MccEventAmqpDto();
                        mccEventAmqpDto.setMccAddress(mcu.getAddress()).
                                setModuleId(timer.getName()).
                                setModule("TIMER").
                                setId(timer.getId()).
                                setValue(timerDto.getOutPutCompareRegister());
                        sendAmqpMessage.sendMessage(mccEventAmqpDto);

                    }
                }

                /*
                search for adcX= 0 ... 7
                 */
                List<ADC> adcs=mcu.getAdcs();
                for (ADC adc: adcs) {
                    logger.info("looping adc "+adc.getAdcId());
                    String key="adc"+adc.getAdcId();
                    if(decodedMessage.containsKey(key)){
                        logger.info("Found adc  "+key+ "in payload...going to update");
                        if(logger.isDebugEnabled()) {
                            logger.debug("Found adc " +key+ "in payload...going to update with value [" + decodedMessage.get(key) + "]");
                        }
                        AdcDto adcDto=new AdcDto();
                        adcDto.setId(adc.getId());
                        adcDto.setAdcId(adc.getAdcId());
                        adcDto.setValue(decodedMessage.get(key));
                        long q=System.currentTimeMillis();
                        adcService.updateAcd(adcDto);
                        long w=System.currentTimeMillis();
                        long e=w-q;
                        logger.info("MCU Addr["+Util.IntegerToByteReadableHex(decodedMessage.get("address"))+"] update ADC took ["+e+"]ms");

                        //Generate Cdr Object
                        Cdr cdr=new Cdr();
                        cdr.setAddr(mcu.getAddress());
                        cdr.setOperation(Operation.ReportUpdate.getValue());
                        cdr.setNow(new Date());
                        cdr.setData("ADC,"+adc.getAdcId()+","+adc.getId()+","+adcDto.getValue());
                        //publish event
                        cdrEventPublisher.generateCdr(this,cdr);

                        //generate Event AMQP
                        MccEventAmqpDto mccEventAmqpDto=new MccEventAmqpDto();
                        mccEventAmqpDto.setMccAddress(mcu.getAddress()).
                                setModuleId("adc"+adc.getAdcId()).
                                setModule("ADC").
                                setId(adc.getId()).
                                setValue(adcDto.getValue());
                        sendAmqpMessage.sendMessage(mccEventAmqpDto);
                    }
                }
                /*
                Update ports
                 */
                List<PORTn> ports=mcu.getIoPort();
                for (PORTn port:ports) {
                    logger.info("looping PORT "+port.getPortName());
                    //TagName == portName
                    if(decodedMessage.containsKey(port.getPortName())){
                        if(logger.isDebugEnabled()) {
                            logger.debug("Found port  " +port.getPortName()+ "in payload...going to update with value [" + Util.IntegerToByteReadableHex(decodedMessage.get(port.getPortName())) + "]");
                        }
                        portnService.updateValue(port.getId(),(byte)(0xFF&decodedMessage.get(port.getPortName())) );

                        //Generate Cdr Object
                        Cdr cdr=new Cdr();
                        cdr.setAddr(mcu.getAddress());
                        cdr.setOperation(Operation.ReportUpdate.getValue());
                        cdr.setNow(new Date());
                        cdr.setData("PORT,"+port.getPortName()+","+port.getId()+","+(0xFF&decodedMessage.get(port.getPortName()))+","+port.getDdb());
                        //publish event
                        cdrEventPublisher.generateCdr(this,cdr);

                        MccEventAmqpDto mccEventAmqpDto=new MccEventAmqpDto();
                        mccEventAmqpDto.setMccAddress(mcu.getAddress()).
                                setModuleId(port.getPortName()).
                                setModule("PORT").
                                setId(port.getId()).
                                setValue((byte)(0xFF&decodedMessage.get(port.getPortName())));
                        sendAmqpMessage.sendMessage(mccEventAmqpDto);

                    }
                }
                ServiceIOMcuStats serviceIOMcuStats=new ServiceIOMcuStats();
                serviceIOMcuStats.setAddress(decodedMessage.get("address"));
                serviceIOMcuStats.setInBoundMessages(1);
                serviceIOMcuStats.setOutBoundMessages(0);
                serviceIOMcuStats.setInBytes(msg.getLength());
                serviceIOMcuStats.setOutBytes(0);
                serviceIOStats.updateMcuStats(serviceIOMcuStats);

            }else{
                logger.error("No MCU found for address "+Util.IntegerToByteReadableHex(decodedMessage.get("address")));
            }
        }
        long end=System.currentTimeMillis();
        long delta=end-start;
        logger.info("MCU Addr["+Util.IntegerToByteReadableHex(decodedMessage.get("address"))+"] Update took ["+delta+"]ms");

    }
}
