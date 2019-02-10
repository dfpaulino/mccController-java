package com.farmtec.mcc.models.modules.timer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {

    Timer timer;
    @Before
    public void setUp() throws Exception {
        timer=new Timer();
        timer.setOutPutCompareRegister(128);
        timer.setMode(TimerMode.FAST_PWM);
    }

    @Test
    public void getOutPutCompareRegister() {
        assertEquals(128,timer.getOutPutCompareRegister());
    }

    @Test
    public void getPWMFreq() {
        timer.getPWMFreq(4,1000000);
        assertEquals(976,timer.getPWMFreq(4,1000000));
    }

    @Test
    public void getPWMpc() {
        assertEquals(50,timer.getPWMpc());
    }
}