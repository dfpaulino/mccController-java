package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.AtmegaType;
import com.farmtec.mcc.models.factory.MccFactory;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import com.farmtec.mcc.repositories.config.RepoConfig;
import com.farmtec.mcc.service.MccService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@Rollback(true)
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
//@DataJpaTest
@SpringBootTest(classes = MccServiceImpl.class)
@EnableTransactionManagement
@TestPropertySource("classpath:application.properties")
@Import(RepoConfig.class)
public class MccServiceImplTest {

    @Autowired
    MccService mccService;

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
        t1.setOutPutCompareRegister(128);

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

        PORTn port=new PORTn();
        port.setDdb((byte)0x00);
        port.setInUse(true);
        port.setPortName("PortA");
        port.setAtmega(atmega);


        List<PORTn> portList = new ArrayList<PORTn>();
        portList.add(port);


        atmega.setAdcs(adcList);
        atmega.setTimers(timerList);
        atmega.setIoPort(portList);

        mccService.createMcu(atmega);
    }

    @Test
    public void getAllMcu() {
        List<Atmega> atmegas = mccService.getAllMcu();
        System.out.println(">>>ATMEGA SIZE "+atmegas.size());
        assertTrue(atmegas.size()>0);
    }

    @Test
    public void getMcuDetailsById() {
        createMcu();
        List<Atmega> atmegas = mccService.getAllMcu();
        Atmega atmegaX=mccService.getMcuDetailsById(atmegas.get(0).getId());
        Atmega atmega=mccService.getMcuDetailsById(atmegaX.getId());
        System.out.println("MCC PWM :"+atmega.getTimers().get(0).getPWMpc());

    }

    @Test
    public void createMcu() {

        int currentAtmegas=mccService.getAllMcu().size();

        Atmega atmega = new Atmega();
        atmega.setName("tankTapController");
        atmega.setAddress("0XABCDA");
        atmega.setModel(AtmegaType.ATMEGA32);

        List<Timer> timerList = new ArrayList<Timer>();
        Timer t1 = new Timer();
        t1.setInUse(true);
        t1.setMode(TimerMode.FAST_PWM);
        t1.setName("timer1");
        t1.setOutPutCompareRegister(128);

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

        PORTn port=new PORTn();
        port.setDdb((byte)0x00);
        port.setInUse(true);
        port.setPortName("PortA");
        port.setAtmega(atmega);


        List<PORTn> portList = new ArrayList<PORTn>();
        portList.add(port);

        atmega.setAdcs(adcList);
        atmega.setTimers(timerList);
        atmega.setIoPort(portList);

        mccService.createMcu(atmega);
        assertEquals(currentAtmegas+1,mccService.getAllMcu().size() );
    }

    @Test
    public void updateMcuNewMcc() {
        Atmega atmega= MccFactory.createMcc("0XBABA",AtmegaType.ATMEGA32,"name");
        atmega.setName("pump1");
        mccService.updateMcu(atmega);
        assertEquals(2,mccService.getAllMcu().size());

    }
    @Test
    public void updateMcu() {
        Atmega atmega= MccFactory.createMcc("0xFFFF",AtmegaType.ATMEGA32,"name2");
        atmega.setName("pump1");
        mccService.updateMcu(atmega);
        assertEquals(2,mccService.getAllMcu().size());

    }

}