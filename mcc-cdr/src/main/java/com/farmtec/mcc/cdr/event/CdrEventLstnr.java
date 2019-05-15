package com.farmtec.mcc.cdr.event;

import com.farmtec.mcc.cdr.dto.Cdr;
import com.farmtec.mcc.cdr.logger.CdrLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CdrEventLstnr implements ApplicationListener<CdrEvent> {

    @Autowired
    CdrLogger cdrLogger;
    @Override
    public void onApplicationEvent(CdrEvent cdrEvent) {
        //System.out.println("Event being processed...");
        cdrLogger.log(cdrEvent.getCdr());
    }
}
