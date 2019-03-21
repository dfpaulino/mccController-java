package com.farmtec.mcc.service.impl;

import com.farmtec.io.handler.MessageSenderService;
import com.farmtec.io.message.Message;
import com.farmtec.io.message.MessageEncoder;

import com.farmtec.mcc.dto.modules.AtmegaDto;
import com.farmtec.mcc.service.MccIpAddressResolver;
import com.farmtec.mcc.service.Operation;
import com.farmtec.mcc.service.ServiceIoSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServiceIoSenderImpl implements ServiceIoSender {


    Logger logger = LoggerFactory.getLogger(ServiceIoSenderImpl.class);

    @Autowired
    MessageSenderService messageSenderService;

    @Autowired
    MccIpAddressResolver mccIpAddressResolver;

    @Override
    public boolean sendMessage(AtmegaDto mccDto, Operation operation) {

        String ipAddr=null;
        if(null!=(ipAddr=mccIpAddressResolver.getIpForMccAdr(mccDto.getAddress())))
        {
            Map<String,Integer> map =new HashMap<String,Integer>();
            Message frame=new MessageEncoder(null);

            frame.encodeMessage(Byte.valueOf(mccDto.getAddress(),16),Operation.UpdateMcu.getValue(),map);
            logger.debug("Sending message");
            return messageSenderService.sendMessage(frame,ipAddr);
        }else{
            return false;
        }
    }
}
