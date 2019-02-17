package com.farmtec.mcc.models.factory;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.AtmegaType;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class MccFactory {
    public static Atmega createMcc(String addr, AtmegaType type,String name)
    {
        Atmega mcc=null;

        switch (type)
        {
            case ATMEGA32:
                mcc=createAtmega32(addr,name);
                break;
            case ATMEGA18:
                mcc=createAtmega18(addr,name);
        }
        return mcc;
    }

    private static Atmega createAtmega32(String addr,String name)
    {
        Atmega mcc=new Atmega();
        mcc.setName(name);
        mcc.setAddress(addr);
        mcc.setModel(AtmegaType.ATMEGA32);
        mcc.setInUse(true);
        List<Timer> timerList = new ArrayList<Timer>();
        Timer t1=new Timer();
        t1.setInUse(true);
        t1.setMode(TimerMode.FAST_PWM);
        t1.setName("timer0");
        t1.setOutPutCompareRegister(128);

        Timer t2=new Timer();
        t2.setInUse(true);
        t2.setMode(TimerMode.FAST_PWM);
        t2.setName("timer1");
        t2.setOutPutCompareRegister(128);

        Timer t3=new Timer();
        t3.setInUse(true);
        t3.setMode(TimerMode.FAST_PWM);
        t3.setName("timer3");
        t3.setOutPutCompareRegister(128);

        t1.setAtmega(mcc);
        t2.setAtmega(mcc);
        t3.setAtmega(mcc);

        timerList.add(t1);
        timerList.add(t2);
        timerList.add(t3);

        mcc.setTimers(timerList);

        List<ADC> adcList=new ArrayList<>();
        for (int x=0;x<8;x++)
        {
            ADC adc=new ADC();
            adc.setAdcId((byte) x);
            adc.setValue(0);
            adc.setAtmega(mcc);
            adc.setInUse(true);
            adcList.add(adc);
        }
        mcc.setAdcs(adcList);

        List<PORTn> IOPortList=new ArrayList<>();
        String[] portNames= new String[]{"PORTA","PORTB","PORTC","PORTD"};
        for (String s:portNames)
        {
            PORTn port=new PORTn();
            port.setPortName(s);
            port.setValue((byte)0x00);
            port.setDdb((byte)0x00);
            port.setAtmega(mcc);
            port.setInUse(true);
            IOPortList.add(port);
        }
        mcc.setIoPort(IOPortList);

        return mcc;
    }

    private static Atmega createAtmega18(String addr,String name) {
        Atmega mcc = new Atmega();
        mcc.setName(name);
        mcc.setAddress(addr);
        mcc.setModel(AtmegaType.ATMEGA18);
        mcc.setInUse(true);
        List<Timer> timerList = new ArrayList<Timer>();
        Timer t1 = new Timer();
        t1.setInUse(true);
        t1.setMode(TimerMode.FAST_PWM);
        t1.setName("timer0");
        t1.setOutPutCompareRegister(128);

        Timer t2 = new Timer();
        t2.setInUse(true);
        t2.setMode(TimerMode.FAST_PWM);
        t2.setName("timer1");
        t2.setOutPutCompareRegister(128);

        Timer t3 = new Timer();
        t3.setInUse(true);
        t3.setMode(TimerMode.FAST_PWM);
        t3.setName("timer3");
        t3.setOutPutCompareRegister(128);

        t1.setAtmega(mcc);
        t2.setAtmega(mcc);
        t3.setAtmega(mcc);

        timerList.add(t1);
        timerList.add(t2);
        timerList.add(t3);

        mcc.setTimers(timerList);

        List<ADC> adcList = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            ADC adc = new ADC();
            adc.setAdcId((byte) x);
            adc.setValue(0);
            adc.setAtmega(mcc);
            adc.setInUse(true);
            adcList.add(adc);
        }
        mcc.setAdcs(adcList);

        List<PORTn> IOPortList = new ArrayList<>();
        String[] portNames = new String[]{"PORTA", "PORTB", "PORTC", "PORTD"};
        for (String s : portNames) {
            PORTn port = new PORTn();
            port.setPortName(s);
            port.setValue((byte) 0x00);
            port.setDdb((byte) 0x00);
            port.setAtmega(mcc);
            port.setInUse(true);
            IOPortList.add(port);
        }
        mcc.setIoPort(IOPortList);

        return mcc;
    }
}
