package com.farmtec.mcc.cdr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(AsynchronousSpringEventsConfig.class)
@Configuration
public @interface EnableCdr {
}
