package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.repositories.AdcRepository;
import com.farmtec.mcc.service.AdcService;
import com.farmtec.mcc.service.MccServiceErrorCode;
import com.farmtec.mcc.service.MccServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class AdcServiceImpl implements AdcService {


    Logger logger = LoggerFactory.getLogger(AdcServiceImpl.class);

    @Autowired
    AdcRepository adcRepository;

    @Override
    @Transactional
    public ADC updateAcd(AdcDto adcDto)throws MccServiceException {
        ADC adc=getAdc(adcDto.getId());
        if(null!=adc)
        {
            if(adcDto.getAdcId() != adc.getAdcId())
            {
                throw  new MccServiceException("ACD ID's dont match", MccServiceErrorCode.DB_NOT_FOUND);
            }
            if(adcDto.getValue() >0)
            {
                adc.setValue(adcDto.getValue());
            }
        }
        return adc;
    }

    @Override
    @Transactional
    public boolean setInUseFlag(int id, boolean inUse) throws MccServiceException {

        ADC adc=getAdc(id);
        if(null!=adc)
        {
            adc.setInUse(inUse);
        }
        return true;
    }
    @Override
    @Transactional
    public ADC getInfo(int id) throws MccServiceException {

        ADC adc=getAdc(id);
        return adc;
    }

    private ADC getAdc(int id)
    {
        ADC adc=null;

        try {
            adc = adcRepository.findById(id).orElse(null);

        }catch (NoSuchElementException noSuchElementException)
        {
            logger.error("No such Adc id "+id);
            throw new MccServiceException(noSuchElementException,"MccId not found", MccServiceErrorCode.DB_NOT_FOUND.getValue());
        }
        if(adc==null)
        {
            logger.error("No such Timer id"+id);
            throw new MccServiceException("AdcId not found", MccServiceErrorCode.DB_NOT_FOUND);

        }

        return adc;
    }
}
