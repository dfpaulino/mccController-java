package com.farmtec.mcc.config;

import com.farmtec.io.handler.MessageHandlerService;
import com.farmtec.mcc.service.Operation;
import com.farmtec.mcc.service.ServiceIoReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = "com.farmtec.mcc")
@EnableScheduling
@EnableMBeanExport
public class ServiceIOConfig {

    @Autowired
    MessageHandlerService messageHandlerService;


    //register the serviceHandler
    @Bean
    ServiceIoReceiver serviceIoReceiver(){
        ServiceIoReceiver serviceIO=new ServiceIoReceiver();
        messageHandlerService.registerHandler("reportUpdate",serviceIO);
        return serviceIO;
    }
}
