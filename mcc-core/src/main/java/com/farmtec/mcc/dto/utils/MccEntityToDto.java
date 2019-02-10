package com.farmtec.mcc.dto.utils;

import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.modules.AtmegaDto;
import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.BaseModel;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MccEntityToDto {

    public static AtmegaDto mccEntityToDtoSimple(Atmega entity)
    {
        AtmegaDto mccDto=new AtmegaDto();
        mccDto.setId(entity.getId());
        mccDto.setModel(entity.getModel());
        mccDto.setAddress(entity.getAddress());
        mccDto.setCreated(entity.getCreated());
        mccDto.setUpdated(entity.getUpdated());
        mccDto.setName(entity.getName());
        mccDto.setInUse(entity.isInUse());
        return mccDto;
    }

    public static AtmegaDto mccEntityToDtoFull(Atmega entity,boolean inUse)
    {
        AtmegaDto mccDto;
        Predicate<BaseModel> filter;

        if(inUse)
        {
            filter=base->base.isInUse();
        }
        else
        {
            filter=base->true;
        }

        mccDto=MccEntityToDto.mccEntityToDtoSimple(entity);
        mccDto.setTimers(entity.getTimers().stream().filter(filter).map(timer ->MccEntityToDto.timerEntityToTimerDto(timer)).collect(Collectors.toList()));
        mccDto.setAdcs(entity.getAdcs().stream().filter(filter).map(adc -> MccEntityToDto.acdEntityToAdcDto(adc)).collect(Collectors.toList()));
        mccDto.setIoPort(entity.getIoPort().stream().filter(filter).map(portX->MccEntityToDto.portEntityToPortDto(portX)).collect(Collectors.toList()));

        return mccDto;
    }


    public static TimerDto timerEntityToTimerDto(Timer entity)
    {
        TimerDto timer=new TimerDto();
        timer.setId(entity.getId());
        timer.setInUse(entity.isInUse());
        timer.setMode(entity.getMode());
        timer.setName(entity.getName());
        timer.setOutPutCompareRegister(entity.getOutPutCompareRegister());
        timer.setCreated(entity.getCreated());
        timer.setUpdated(entity.getUpdated());
        return timer;
    }

    public static AdcDto acdEntityToAdcDto(ADC entity)
    {
        AdcDto adcDto=new AdcDto();
        adcDto.setId(entity.getId());
        adcDto.setAdcId(entity.getAdcId());
        adcDto.setValue(entity.getValue());
        adcDto.setInUse(entity.isInUse());
        return adcDto;

    }

    public static PORTnDto portEntityToPortDto(PORTn entity)
    {
        PORTnDto porTnDto= new PORTnDto();
        porTnDto.setId(entity.getId());
        porTnDto.setPortName(entity.getPortName());
        porTnDto.setDdb(entity.getDdb());
        porTnDto.setValue(entity.getValue());
        porTnDto.setInUse(entity.isInUse());
        return porTnDto;

    }
}
