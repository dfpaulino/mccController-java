package com.farmtec.mcc.repositories;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.AtmegaType;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import com.farmtec.mcc.repositories.config.RepoConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.Rollback;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest(classes = MccRepository.class)
@EnableAutoConfiguration
@Transactional
@Rollback(false)
@RunWith(SpringRunner.class)
@DataJpaTest
@Import({RepoConfig.class})
public class mccRepositoryTest {

    @Autowired
    MccRepository mccRepository;
    @Before
    public void initSetUp() {

        Atmega atmega = new Atmega();
        atmega.setName("tankTapController");
        atmega.setAddress("0XABCD");
        atmega.setModel(AtmegaType.ATMEGA32);

        List<Timer> timerList = new ArrayList<Timer>();
        Timer t1 = new Timer();
        t1.setInUse(true);
        t1.setMode(TimerMode.FAST_PWM);
        t1.setName("timer1");
        t1.setOutPutCompareRegister(240);

        Timer t2 = new Timer();
        t2.setInUse(true);
        t2.setMode(TimerMode.PHASE_CORRECT_PWM);
        t2.setName("timer2");
        t2.setOutPutCompareRegister(128);

        t1.setAtmega(atmega);
        t2.setAtmega(atmega);

        timerList.add(t1);
        timerList.add(t2);


        List<ADC> adcList = new ArrayList<ADC>();
        ADC adc1 = new ADC();
        adc1.setAdcId((byte) 1);
        adc1.setValue(38);


        ADC adc2 = new ADC();
        adc2.setAdcId((byte) 1);
        adc2.setValue(38);

        adc1.setAtmega(atmega);
        adc2.setAtmega(atmega);
        adcList.add(adc1);
        adcList.add(adc2);


        atmega.setAdcs(adcList);
        atmega.setTimers(timerList);

        mccRepository.save(atmega);
        mccRepository.flush();
    }
    @Test
    @Rollback
    public void saveAtmegaTest1() {


        Atmega atmega1= new Atmega();
        atmega1.setName("tankTapController2");
        atmega1.setAddress("0XABCE");
        atmega1.setModel(AtmegaType.ATMEGA32);

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

        t1.setAtmega(atmega1);
        t2.setAtmega(atmega1);

        timerList.add(t1);
        timerList.add(t2);



        List<ADC> adcList = new ArrayList<ADC>();
        ADC adc1=new ADC();
        adc1.setAdcId((byte) 1);
        adc1.setValue(38);


        ADC adc2=new ADC();
        adc2.setAdcId((byte) 1);
        adc2.setValue(38);

        adc1.setAtmega(atmega1);
        adc2.setAtmega(atmega1);
        adcList.add(adc1);
        adcList.add(adc2);



        atmega1.setAdcs(adcList);
        atmega1.setTimers(timerList);

        mccRepository.save(atmega1);
        mccRepository.flush();

       Optional<Atmega> atmegaOp=mccRepository.findById(atmega1.getId());
       Atmega atmegap=atmegaOp.get();
       System.out.println("Address is "+atmegap.getAddress());
       List<Timer> list = atmegap.getTimers();
       System.out.println("pwm is "+list.get(0).getPWMpc());
       assertEquals(94,list.get(0).getPWMpc());

    }

    @Test
    public void getAdcValueTest2()
    {
        List<Atmega> atmegas=mccRepository.findAll();
        System.out.println("Size fo Aray" + atmegas.size());
        if(atmegas.size()>0)
        {
            assertEquals(38,atmegas.get(0).getAdcs().get(0).getValue());
        }
        else{
            assertFalse((atmegas.size()==0));
        }


    }
    @Test
    public void updateTimerTest3()
    {
        List<Atmega> atmegas=mccRepository.findAll();
        System.out.println("updating timer "+ atmegas.get(0).getTimers().get(1).getId());
        atmegas.get(0).getTimers().get(1).setOutPutCompareRegister(240);
        System.out.println("after update");
        assertEquals(94,atmegas.get(0).getTimers().get(1).getPWMpc());

    }

    @Test
    public void updateAtmegaNameTest4()
    {
        Date now=new Date();
        System.out.println(">>>>>>>>>>>>>Now "+now.toString());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Atmega> atmegas=mccRepository.findAll();
        atmegas.get(0).setName("Funny");
        System.out.println("<<<<<<<<<<<<<<<"+atmegas.get(0).getTimers().get(0).getUpdated());

    }
}