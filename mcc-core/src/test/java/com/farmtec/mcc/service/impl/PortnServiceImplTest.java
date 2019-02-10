package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.repositories.PortRepository;
import com.farmtec.mcc.repositories.config.RepoConfig;
import com.farmtec.mcc.service.MccServiceException;
import com.farmtec.mcc.service.PortnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PortnServiceImpl.class)
@Rollback(true)
@EnableAutoConfiguration
@EnableTransactionManagement
@TestPropertySource("classpath:application.properties")
@Import(RepoConfig.class)
//Load atmega model into database for test
@Sql(scripts = {"classpath:data-h2.sql"})
public class PortnServiceImplTest {

    @Autowired
    PortnService portnService;

    @Autowired
    PortRepository portRepository;

    @Test
    public void updateValue() {
        List<PORTn> porTnList=portRepository.findAll();
        byte value = (byte) 0xFF;
        PORTn myPortN=portnService.updateValue(porTnList.get(0).getId(),value);
        System.out.println("Value 0X"+Integer.toHexString(myPortN.getValue() & 0xFF));
        assertEquals((byte)0xFF,porTnList.get(0).getValue());
    }

    @Test(expected= MccServiceException.class)
    public void updateValueInvalidId() {

        byte value = (byte) 0xFF;
        PORTn myPortN=portnService.updateValue(1000,value);
        System.out.println("Value 0X"+Integer.toHexString(myPortN.getValue() & 0xFF));
        assertEquals((byte)0xFF,myPortN.getValue());
    }


    @Test
    public void updateDDR() {
        List<PORTn> porTnList=portRepository.findAll();
        byte value = (byte) 0xAA;
        PORTn myPortN=portnService.updateDDR(porTnList.get(1).getId(),value);
        System.out.println("Value 0X"+Integer.toHexString(myPortN.getDdb() & 0xFF));

        assertEquals((byte)0xAA,porTnList.get(1).getDdb());
    }

    @Test
    public void updateInUse() {
        List<PORTn> porTnList=portRepository.findAll();
        byte value = (byte) 0xFF;
        PORTn myPortN=portnService.updateInUse(porTnList.get(0).getId(),false);
        System.out.println("Value 0X"+Integer.toHexString(myPortN.getValue() & 0xFF));

        assertEquals(false,porTnList.get(0).isInUse());
    }
}