package com.farmtec.amqp.client.producer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
public class AmqpExtraConfig {

    @Bean
    SimpleRabbitListenerContainerFactory container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleRabbitListenerContainerFactory container = new SimpleRabbitListenerContainerFactory();
        container.setConnectionFactory(connectionFactory);

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


    @Bean
    Queue queue() {
        return new Queue("myTestQ", true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("mccUpdateExchange");
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange)  {
        return BindingBuilder.bind(queue).to(exchange).with("mccUpdateRoutingKey");
    }

}
