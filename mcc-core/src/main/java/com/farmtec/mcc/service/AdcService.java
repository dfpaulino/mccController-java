package com.farmtec.mcc.service;

import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.models.modules.adc.ADC;

public interface AdcService {

    public ADC updateAcd(AdcDto acdDtp)throws MccServiceException;;
    public boolean setInUseFlag(int id, boolean inUse) throws MccServiceException;
    public ADC getInfo(int id) throws MccServiceException;
}
