package com.farmtec.mcc.service;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MccService {

    /*
    Get All Mcc details Only, not data of the Modules
     */
    List<Atmega> getAllMcu();
    /*
    returns a Copy of the entity with the Modules info
    This is the complete data
     */
    Atmega getMcuDetailsById(int id);

    List<Timer> getTimersInfoByMcuId(int id);
    boolean updateTimersByMcuId(int id,List<Timer> timers)throws MccServiceException;
    void updateTimersById(int id,Timer timers)throws MccServiceException;


    List<ADC> getADCsInfoByMcuId(int id);
    void updateADCByMcuId(int id,List<ADC> adcs);
    void updateADCId(int id,ADC adc);

    List<PORTn> getPORTnInfoByMcuId(int id);
    void updatePortsByMcuId(int id,List<PORTn> ports)throws MccServiceException;
    void updatePortXId(int id,PORTn portX)throws MccServiceException;


    Atmega createMcu(Atmega atmega) throws MccServiceException;
    Atmega updateMcu(Atmega atmega) throws MccServiceException;

    void deleteMcc(int id);

}
