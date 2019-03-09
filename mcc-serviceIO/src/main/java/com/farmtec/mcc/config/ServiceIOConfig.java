package com.farmtec.mcc.config;

import com.farmtec.io.handler.MessageHandlerService;
import com.farmtec.mcc.service.ServiceIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.farmtec.mcc")
public class ServiceIOConfig {

    @Autowired
    MessageHandlerService messageHandlerService;

    //register the serviceHandler
    @Bean
    ServiceIO getServiceIO(){
        ServiceIO serviceIO=new ServiceIO();
        messageHandlerService.registerHandler("reportUpdate",serviceIO);
        return serviceIO;
    }
}
