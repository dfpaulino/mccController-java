package com.farmtec.mcc.cdr.event;

import com.farmtec.mcc.cdr.dto.Cdr;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CdrEvent extends ApplicationEvent {

    private Cdr cdr;

    public CdrEvent(Object source, Cdr cdr) {
        super(source);
        this.cdr = cdr;
    }
}
