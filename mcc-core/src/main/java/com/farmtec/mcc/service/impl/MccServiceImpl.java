package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.repositories.MccRepository;
import com.farmtec.mcc.service.MccService;

import com.farmtec.mcc.service.MccServiceErrorCode;
import com.farmtec.mcc.service.MccServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;

@Service
@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
public class MccServiceImpl implements MccService {

    Logger logger = LoggerFactory.getLogger(MccService.class);

    @Autowired
    MccRepository mccRepository;

    @Override
    @Transactional(timeout = 5)
    public List<Atmega> getAllMcu() {
        return mccRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Atmega getMcuDetailsById(int id) {

        Atmega mcc=null;
        //get the mcc info
         //mccRepoh is in lazy mode
        logger.info("Find Atmega for id "+id);
        Atmega mccRepo=mccRepository.findById(id).orElse(null);
        if(null!=mccRepo) {

            //gto force fetch of modules under this transaction
            int numberOfTimers=mccRepo.getTimers().size();
            int numberOfADCs=mccRepo.getAdcs().size();
            int numberOfPORTs=mccRepo.getIoPort().size();

            if(logger.isDebugEnabled()) {
                logger.debug("Atmega ID["+id+"] found. Timers ["+numberOfTimers+"] ADC's ["+numberOfADCs+"] PORTS ["+numberOfPORTs+"]");
            }


        }

         return mccRepo;
    }

    //TODO implement other methods
    @Override
    public List<Timer> getTimersInfoByMcuId(int id) {
        return mccRepository.findById(id).get().getTimers();
    }

    @Override
    @Transactional
    public boolean updateTimersByMcuId(int id, List<Timer> timers) throws MccServiceException {

        boolean result=false;

        Atmega atmega=null;
        try
        {
            atmega=getMcuDetailsById(id);
        }
        catch(EmptyResultDataAccessException e)
        {
            logger.error(" Cant find Mcc Id ["+id+"]Exception[ "+e.getMessage()+"] stack trace:"+e.getStackTrace().toString());
            e.printStackTrace();
        }
        if(null==atmega)
        {
            throw new MccServiceException("MccId not found",MccServiceErrorCode.DB_NOT_FOUND);
        }

        try{
            atmega.updateTimers(timers);
            logger.info("Timers for Mcc Id  "+id+" updated");
            result=true;
        }
        catch (DataAccessException dae)
        {
            logger.error("Exception[ "+dae.getMessage()+"] stack trace:"+dae.getStackTrace().toString());
            dae.printStackTrace();

        }
        return result;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTimersById(int id, Timer timers) {
        /*
        get list of timers for the MccId and update the values
         */
    }

    @Override
    public List<ADC> getADCsInfoByMcuId(int id) {
        return mccRepository.findById(id).get().getAdcs();
    }

    @Override
    public void updateADCByMcuId(int id, List<ADC> adcs) {

    }

    @Override
    public void updateADCId(int id, ADC adc) {

    }

    @Override
    public List<PORTn> getPORTnInfoByMcuId(int id) {
        return mccRepository.findById(id).get().getIoPort();
    }

    @Override
    public void updatePortsByMcuId(int id, List<PORTn> ports) {

    }

    @Override
    public void updatePortXId(int id, PORTn portX) {

    }

    //TODO handle exception properly!!
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Atmega createMcu(Atmega atmega) {
        Atmega mcc=null;
        boolean alreadyEsists=false;

        try {
            mcc = mccRepository.findByAddress(atmega.getAddress());
        }
        catch(EmptyResultDataAccessException e)
        {

        }

        try {

            if (null != mcc) {
                logger.info("Mxx with address [ " + atmega.getAddress() + " ] already exists. going to update instead");
                //mcc=this.updateMcu(atmega);
                alreadyEsists=true;

            } else {
                mcc = mccRepository.save(atmega);
            }
        }
        catch (DataAccessException dae)
        {
            logger.error("Exception[ "+dae.getMessage()+"] stack trace:"+dae.getStackTrace().toString());
            dae.printStackTrace();
            throw new MccServiceException(dae,"Unable to create Mcc", MccServiceErrorCode.DB_ERROR.getValue());
        }
       // if(alreadyEsists)
       // {
        //    throw new MccServiceException("Mcc address already exists", MccServiceErrorCode.DB_ERROR);
       // }
        return mcc;
    }


    //TODO handle exception properly!!
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Atmega updateMcu(Atmega atmega) {

        Atmega mcc=null;
        try {
             mcc=mccRepository.findByAddress(atmega.getAddress());
            if(null!=mcc) {
                if(mcc.updateValues(atmega))
                {
                    logger.info("Mxx with address [ "+atmega.getAddress()+" ] will be updated!");
                    mccRepository.saveAndFlush(mcc);
                }
                else
                {
                    logger.error("Mxx with address [ "+atmega.getAddress()+" ] cant be updated!");
                }
            }
            else
            {
                logger.info("Mxx with address [ "+atmega.getAddress()+" ] does not exist ...saving new entity");
                mcc= mccRepository.save(atmega);
            }
        }
        catch (EmptyResultDataAccessException e)
        {
            logger.warn("Mxx with address [ "+atmega.getAddress()+" ] does not exist...saving new entity");
            mcc= mccRepository.save(atmega);
        }
        catch (DataAccessException dae)
        {
            logger.error("Exception[ "+dae.getMessage()+"] stack trace:"+dae.getStackTrace().toString());

        }
        return mcc;
    }
    /*
    All methods should be Transactional as the repository is configuread as Lazy featch
     */

    public void deleteMcc(int id)
    {
        try{
            mccRepository.deleteById(id);

        }catch (DataAccessException dae)
        {
            logger.error("Exception[ "+dae.getMessage()+"] stack trace:"+dae.getStackTrace().toString());

        }
    }
}
