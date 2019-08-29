package com.farmtec.amqp;

import com.farmtec.amqp.config.AmqpConfig;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(AmqpConfig.class)
@Configuration
//@EnableRabbit
public @interface EnableAMQP {
}
