package com.farmtec.mcc.service;

import com.farmtec.io.handler.BasicHandler;
import com.farmtec.io.handler.MessageHandlerService;
import com.farmtec.io.message.Message;
import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.utils.Util;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class ServiceIO extends BasicHandler {
    Logger logger = LoggerFactory.getLogger(MessageHandlerService.class);

    @Autowired
    MccService mccService;
    @Autowired
    AdcService adcService;
    @Autowired
    TimerService timerService;
    @Autowired
    PortnService portnService;

    @Override
    public void execute(Message msg) {

        Map<String,Integer> decodedMessage=msg.decodeToMap();
        if (logger.isInfoEnabled())
            logger.info("Processing message "+ msg.toString());
        // this is the MCU sending its report Update
        if(Operation.ReportUpdate.getValue()==(byte)( decodedMessage.get("operation")&0xFF)){
            logger.info("Processing ReportUpdate for MCU Addr "+ Util.IntegerToByteReadableHex(decodedMessage.get("address")) );
            Atmega mcu=mccService.getMcuDetailsByAddress(Util.IntegerToByteReadableHex(decodedMessage.get("address")));
            if(null!=mcu){
                /*
                search for timerX X= 0 1 2 A
                timer0 timer1 timer2
                 */
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
                        adcDto.setValue(decodedMessage.get(key));
                        adcService.updateAcd(adcDto);
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
                    }
                }
            }else{
                logger.error("No MCU found for address "+Util.IntegerToByteReadableHex(decodedMessage.get("address")));
            }


        }
    }
}
