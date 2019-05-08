package com.farmtec.mcc.service.impl;

import com.farmtec.io.handler.MessageSenderService;
import com.farmtec.io.message.Message;
import com.farmtec.io.message.MessageEncoder;

import com.farmtec.mcc.dto.modules.AtmegaDto;
import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.service.MccIpAddressResolver;
import com.farmtec.mcc.service.Operation;
import com.farmtec.mcc.service.ServiceIoSender;
import com.farmtec.mcc.stats.ServiceIOMcuStats;
import com.farmtec.mcc.stats.ServiceIOStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceIoSenderImpl implements ServiceIoSender {


    Logger logger = LoggerFactory.getLogger(ServiceIoSenderImpl.class);

    @Autowired
    MessageSenderService messageSenderService;

    @Autowired
    MccIpAddressResolver mccIpAddressResolver;
    @Autowired
    ServiceIOStats serviceIOStats;

    @Override
    public boolean sendMessage(AtmegaDto mccDto, Operation operation) {

        String ipAddr=null;
        if(null!=(ipAddr=mccIpAddressResolver.getIpForMccAdr(mccDto.getAddress())))
        {
            Map<String,Integer> map =encodeMcuToTagValue(mccDto);
            Message frame=new MessageEncoder(null);

            frame.encodeMessage(Byte.valueOf(mccDto.getAddress(),16),Operation.UpdateMcu.getValue(),map);
            logger.debug("Sending message ["+((MessageEncoder) frame).printBuffer()+"]");

            ServiceIOMcuStats serviceIOMcuStats=new ServiceIOMcuStats();
            serviceIOMcuStats.setAddress(Byte.valueOf(mccDto.getAddress(),16));
            serviceIOMcuStats.setInBoundMessages(0);
            serviceIOMcuStats.setOutBoundMessages(1);
            serviceIOMcuStats.setInBytes(0);
            serviceIOMcuStats.setOutBytes(frame.getLength());
            serviceIOStats.updateMcuStats(serviceIOMcuStats);

            return messageSenderService.sendMessage(frame,ipAddr);
        }else{
            return false;
        }
    }

    private Map<String,Integer> encodeMcuToTagValue(AtmegaDto mccDto){
        /**
         * From the design the name of the components match the message tagName
         */
        /**
         *
         *  protected void loadTagEncoderMap(){
         *         tagEncoder = Stream.of(new Object[][] {
         *                 { "length", 0x80 },{ "operation",0x81 },
         *                 { "address",0x82 },{ "timer0",0x83 },
         *                 { "timer1",0x84 },{ "timer2",0x85 },
         *                 { "portA",0x86 },{ "portB",0x87 },
         *                 { "portC",0x88 },{ "portD",0x89 },
         *                 { "adc0",0x8a },{ "adc1",0x8b },
         *                 { "adc2",0x8c },{ "adc3",0x8d },
         *                 { "adc4",0x8e },{ "adc5",0x8f },
         *                 { "adc6",0x90 },{ "adc7",0x91 },
         */
        Map<String,Integer> map =new HashMap<String,Integer>();
        //loop timers
        if(null!=mccDto.getTimers()) {
            for (TimerDto timerDto : mccDto.getTimers()) {
                map.put(timerDto.getName(), timerDto.getOutPutCompareRegister());
            }
        }

        //loop ports
        if(null!=mccDto.getIoPort()) {
            for (PORTnDto porTnDto : mccDto.getIoPort()) {
                map.put(porTnDto.getPortName(), (int) porTnDto.getValue());
            }
        }

        //This is outbound messages, so ADC's are never written, only read
        return map;
    }
}
