package com.farmtec.mcc.dto.mappers;

import com.farmtec.mcc.dto.BaseDto;
import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.BaseModel;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import org.mapstruct.*;

public class MccMappers {

    public interface GenericMapper<T,R> {
        R map(T t);
        T reverseMap(R r);
    }

    @Mapper()
    public interface TimerMapper extends GenericMapper<Timer,TimerDto>{
        TimerDto map (Timer timer);
        Timer reverseMap(TimerDto timerDto);
    }


    @Mapper
    public interface AdcMapper extends GenericMapper<ADC, AdcDto> {
        AdcDto map(ADC adc);
        ADC reverseMap(AdcDto adcDto);
    }

    @Mapper
    public interface PortIoMapper extends GenericMapper<PORTn, PORTnDto> {
        PORTnDto map(PORTn porTn);
        PORTn reverseMap(PORTnDto porTnDto);
    }


}
