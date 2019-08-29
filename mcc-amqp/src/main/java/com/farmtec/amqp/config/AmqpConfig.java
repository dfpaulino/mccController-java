package com.farmtec.amqp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

@Configuration
@ComponentScan(basePackages = "com.farmtec.amqp")
public class AmqpConfig {

    @Value("${spring.rabbitmq.host:localhost}")
    String amqpHost;
    @Value("${pring.rabbitmq.port:5672}")
    int amqpPort;
    @Value("${spring.rabbitmq.username:guest}")
    String amqpUser;
    @Value("${spring.rabbitmq.password:guest}")
    String amqpPass;

    @Value("${amqp.exchange.name:mccUpdateExchange}")
    String sendToExchange;

    @Value("${amqp.routing.key:mccUpdateRoutingKey}")
    String routingKey;

    @Bean
    AmqpAdmin amqpAdmin(){
        return new RabbitAdmin(getConnectionFactory());
    }

    @Bean
    public ConnectionFactory getConnectionFactory(){
        CachingConnectionFactory cnxFactory=new CachingConnectionFactory();
        cnxFactory.setHost(amqpHost);
        cnxFactory.setPort(amqpPort);
        cnxFactory.setUsername(amqpUser);
        cnxFactory.setPassword(amqpPass);
        cnxFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        cnxFactory.setChannelCacheSize(10);
        cnxFactory.setVirtualHost("/");
        return cnxFactory;
    }

    @Bean
    public RabbitTemplate getRabbitTemplate(){
        RabbitTemplate rabbitTemplate=new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(getConnectionFactory());
        rabbitTemplate.setExchange(sendToExchange);
        rabbitTemplate.setRoutingKey(routingKey);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
