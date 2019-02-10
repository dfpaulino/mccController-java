package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.repositories.AdcRepository;
import com.farmtec.mcc.repositories.config.RepoConfig;
import com.farmtec.mcc.service.AdcService;
import org.junit.Before;
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
@SpringBootTest(classes = AdcServiceImpl.class)
@Rollback(true)
@EnableAutoConfiguration
@EnableTransactionManagement
@TestPropertySource("classpath:application.properties")
@Import(RepoConfig.class)
@Sql(scripts = {"classpath:data-h2.sql"})
public class AdcServiceImplTest {

    @Autowired
    AdcRepository adcRepository;

    @Autowired
    AdcService adcService;


    @Before
    public void setUp(){

    }

    @Test
    public void updateAcd() {
        List<ADC> adcList=adcRepository.findAll();
        AdcDto adcDto=new AdcDto();
        adcDto.setId(adcList.get(0).getId());
        adcDto.setValue(50);
        adcService.updateAcd(adcDto);
        assertEquals(adcDto.getValue(),adcRepository.findById(adcDto.getId()).get().getValue());
    }

    @Test
    public void setInUseFlag() {
        List<ADC> adcList=adcRepository.findAll();
        adcService.setInUseFlag(adcList.get(1).getId(),false);
        assertEquals(false,adcList.get(1).isInUse());

    }
}