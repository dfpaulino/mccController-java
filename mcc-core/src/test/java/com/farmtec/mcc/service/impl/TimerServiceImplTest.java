package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import com.farmtec.mcc.repositories.TimerRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class TimerServiceImplTest {

    @Mock
    TimerRepository timerRepositoryMock;

    @InjectMocks
    TimerServiceImpl timerService;

    Timer timer;

    @Before
    public void setUp()
    {
        timer=new Timer();
        timer.setId(1);
        timer.setOutPutCompareRegister(200);
        timer.setInUse(true);
        timer.setMode(TimerMode.PHASE_CORRECT_PWM);
        timer.setName("timer0");
    }

    @Test
    public void updateTimerById() {
        when(timerRepositoryMock.getOne(any(Integer.class))).thenReturn(timer);
        TimerDto timerDto=new TimerDto();
        timerDto.setId(1);

        timerDto.setOutPutCompareRegister(201);
        timerDto.setInUse(true);
        timerDto.setMode(TimerMode.FAST_PWM);
        timerDto.setName("timer0");
        Timer myTmr=timerService.updateTimerById(timerDto);
        assertEquals(timerDto.getOutPutCompareRegister(),myTmr.getOutPutCompareRegister());
        assertEquals(timerDto.getMode().getValue(),myTmr.getMode().getValue());
    }

    @Test
    @Ignore
    public void setInUseFlag() {
    }

    @Test
    @Ignore
    public void setModeById() {
    }

    @Test
    @Ignore
    public void getInfo() {
    }
}