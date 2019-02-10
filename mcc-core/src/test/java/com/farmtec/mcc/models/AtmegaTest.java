package com.farmtec.mcc.models;

import com.farmtec.mcc.models.factory.MccFactory;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AtmegaTest {

    Atmega atmega;


    @Before
    public void setUp() throws Exception {

         atmega= MccFactory.createMcc("F0DA",AtmegaType.ATMEGA32,"name");



        atmega.getTimers().get(0).setOutPutCompareRegister(240);
        atmega.getTimers().get(0).setInUse(true);
        atmega.getTimers().get(0).setMode(TimerMode.FAST_PWM);

        atmega.getTimers().get(1).setOutPutCompareRegister(128);
        atmega.getTimers().get(1).setInUse(true);
        atmega.getTimers().get(1).setMode(TimerMode.PHASE_CORRECT_PWM);

        atmega.getAdcs().get(0).setValue(38);

    }

    @Test
    public void getNumberOfTimers() {
        List<Timer> timers=atmega.getTimers();
        assertEquals(3,timers.size());

    }

    @Test
    public void getTimer0Pwd() {
        List<Timer> timers=atmega.getTimers();
        assertEquals(94,timers.get(0).getPWMpc());

    }
    @Test
    public void getTimer0PWMFreq() {
        List<Timer> timers=atmega.getTimers();
        assertEquals(976,timers.get(0).getPWMFreq(4,1000000));
    }

    @Test
    public void updateFail()
    {
        Atmega atmega2=new Atmega();
        atmega2.setAddress("0xF0DB");
        List<Timer> timerList = new ArrayList<Timer>();
        Timer t1=new Timer();
        t1.setInUse(true);
        t1.setMode(TimerMode.FAST_PWM);
        t1.setName("timer1");
        t1.setOutPutCompareRegister(240);

        Timer t2=new Timer();
        t2.setInUse(true);
        t2.setMode(TimerMode.PHASE_CORRECT_PWM);
        t2.setName("timer2");
        t2.setOutPutCompareRegister(128);
        timerList.add(t1);
        timerList.add(t2);

        atmega2.setTimers(timerList);

        ADC adc1=new ADC();
        adc1.setAdcId((byte) 1);
        adc1.setValue(38);
        adc1.setAtmega(atmega2);

        assertFalse(atmega.updateValues(atmega2));

    }


    @Test
    public void updateFailDifferentTimersSize()
    {
        Atmega atmega2=new Atmega();
        atmega2.setAddress("0xF0DB");
        List<Timer> timerList = new ArrayList<Timer>();
        Timer t1=new Timer();
        t1.setInUse(true);
        t1.setMode(TimerMode.FAST_PWM);
        t1.setName("timer1");
        t1.setOutPutCompareRegister(240);

        Timer t2=new Timer();
        t2.setInUse(true);
        t2.setMode(TimerMode.PHASE_CORRECT_PWM);
        t2.setName("timer2");
        t2.setOutPutCompareRegister(128);
        timerList.add(t1);
        //timerList.add(t2);

        atmega2.setTimers(timerList);

        ADC adc1=new ADC();
        adc1.setAdcId((byte) 1);
        adc1.setValue(38);
        adc1.setAtmega(atmega2);

        //assertFalse(atmega.updateValues(atmega2));
        assertEquals(false,atmega.updateValues(atmega2));

    }


    @Test
    public void updateFailAddress()
    {
        Atmega atmega2= MccFactory.createMcc("F0DB",AtmegaType.ATMEGA32,"name1");
        //assertFalse(atmega.updateValues(atmega2));
        System.out.println("Atmega ADC size "+atmega2.getAdcs().size());
        assertEquals(false,(atmega.updateValues(atmega2)));

    }

    @Test
    public void updateOk()
    {
        Atmega atmega2= MccFactory.createMcc("F0DA",AtmegaType.ATMEGA32,"name3");
        //assertFalse(atmega.updateValues(atmega2));
        System.out.println("Atmega ADC values "+atmega2.getAdcs().get(1).getValue());
        assertEquals(true,(atmega.updateValues(atmega2)&&atmega.getAdcs().get(0).getValue()==0));

    }
}