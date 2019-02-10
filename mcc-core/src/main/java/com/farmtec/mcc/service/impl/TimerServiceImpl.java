package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import com.farmtec.mcc.repositories.TimerRepository;
import com.farmtec.mcc.service.MccServiceErrorCode;
import com.farmtec.mcc.service.MccServiceException;
import com.farmtec.mcc.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimerServiceImpl implements TimerService {


    @Autowired
    TimerRepository timerRepository;

    @Override
    @Transactional
    public Timer updateTimerById(TimerDto timerDto) throws MccServiceException {

        Timer timer=null;
        if(timerDto.getId()>=0)
        {
            timer=timerRepository.getOne(timerDto.getId());
            if(timer!=null)
            {
                if(timerDto.getOutPutCompareRegister()>0)
                {
                    timer.setOutPutCompareRegister(timerDto.getOutPutCompareRegister());
                }
                if(timerDto.getMode()!=null)
                {
                    if(timerDto.getMode().getValue()>0)
                        timer.setMode(timerDto.getMode());
                }
            }
            else{
                throw  new MccServiceException("Timer not Found", MccServiceErrorCode.DB_NOT_FOUND);
            }
        }
        return timer;
    }

    @Override
    public boolean setInUseFlag(int id, boolean inUse) throws MccServiceException {

        Timer timer=timerRepository.findById(id).get();

        if(timer==null)
        {
            throw  new MccServiceException("Timer not Found", MccServiceErrorCode.DB_NOT_FOUND);
        }
        timer.setInUse(inUse);
        return true;
    }

    @Override
    public boolean setModeById(int id, TimerMode mode) throws MccServiceException {
        Timer timer=timerRepository.getOne(id);
        if(timer==null)
        {
            throw  new MccServiceException("Timer not Found", MccServiceErrorCode.DB_NOT_FOUND);
        }
        timer.setMode(mode);
        return true;
    }

    @Override
    public Timer getInfo(int id) {
        Timer timer=timerRepository.findById(id).get();
        if(timer==null)
        {
            throw  new MccServiceException("Timer not Found", MccServiceErrorCode.DB_NOT_FOUND);
        }
        return timer;
    }


}
