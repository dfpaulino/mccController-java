package com.farmtec.mcc.cdr.event;

import com.farmtec.mcc.cdr.dto.Cdr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CdrEventPublisher {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public void generateCdr(Object source,Cdr cdr){
        //System.out.println("Generating CDR event");
        CdrEvent cdrEvent=new CdrEvent(source,cdr);
        applicationEventPublisher.publishEvent(cdrEvent);
    }
}
