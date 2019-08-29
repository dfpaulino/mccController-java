package com.farmtec.amqp.client.producer;

import com.farmtec.amqp.EnableAMQP;
import com.farmtec.amqp.client.dto.MccEventAmqpDto;
import com.farmtec.amqp.config.AmqpConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.Thread.*;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SendAmqpMessage.class})
@EnableAMQP
public class SendAmqpMessageTest {

    @Autowired
    SendAmqpMessage sendAmqpMessage;
    @Before
    public void setup() {
    }

    @Test
    public void sendMessage() {
        MccEventAmqpDto mccEventAmqpDto=new MccEventAmqpDto();
        mccEventAmqpDto.setAddress("f0");
        mccEventAmqpDto.setModule("ModuleName");
        mccEventAmqpDto.setModuleId("ModuleId");
        mccEventAmqpDto.setValue(128);
        try {
            Thread.sleep(1000);
        }catch (Exception e ){}
        sendAmqpMessage.sendMessage(mccEventAmqpDto);

    }
}