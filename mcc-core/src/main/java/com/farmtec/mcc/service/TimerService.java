package com.farmtec.mcc.service;


import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;

public interface TimerService {
    public Timer updateTimerById(TimerDto timerDto)throws MccServiceException;
    public boolean setInUseFlag(int id, boolean inUse)throws MccServiceException;
    public boolean setModeById (int id, TimerMode mode)throws MccServiceException;
    public  Timer getInfo(int id);

}
