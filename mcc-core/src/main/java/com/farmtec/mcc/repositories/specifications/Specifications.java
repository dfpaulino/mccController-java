package com.farmtec.mcc.repositories.specifications;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.modules.timer.Timer;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {
    public Specification<Timer> getTimerFromTimerName(String timerName)
    {
        return (timer,cq,cb) -> cb.equal(timer.get("name"),timerName);
    }
}
