package com.farmtec.amqp.client.producer;

import com.farmtec.amqp.client.dto.MccEventAmqpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SendAmqpMessage {
    @Autowired
    RabbitTemplate rabbitTemplate;

   private static Logger logger = LoggerFactory.getLogger(SendAmqpMessage.class);

    public void sendMessage(MccEventAmqpDto message){
        logger.info("sending message "+message.toString()+ "to rabbitMQ" );
        rabbitTemplate.convertAndSend(message);
    }
}
