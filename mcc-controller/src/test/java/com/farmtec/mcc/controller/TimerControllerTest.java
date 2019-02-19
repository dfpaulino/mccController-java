package com.farmtec.mcc.controller;

import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.AtmegaType;
import com.farmtec.mcc.models.factory.MccFactory;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import com.farmtec.mcc.repositories.TimerRepository;
import com.farmtec.mcc.service.MccService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;

@Transactional
@Rollback(true)
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
//@DataJpaTest
@SpringBootTest()
@EnableTransactionManagement
@TestPropertySource("classpath:application.properties")
//@Import(RepoConfig.class)
@AutoConfigureMockMvc
public class TimerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TimerRepository timerRepository;

    @Autowired
    TimerController timerController;

    @Autowired
    MccService mccService;

    @Test
    public void contexLoads() throws Exception {
        assertThat(timerController).isNotNull();
    }
    @Test
    public void updateTimer() throws Exception {
        createTimers();
        int tmrId=timerRepository.findAll().get(0).getId();
        String uri="/timer";
        TimerDto tmrDto=new TimerDto();
        tmrDto.setId(tmrId);
        tmrDto.setMode(timerRepository.findAll().get(0).getMode());
        tmrDto.setOutPutCompareRegister(128);

        String tmrDtoJson = new ObjectMapper().writeValueAsString(tmrDto);

        mockMvc.perform(put(uri).contentType(MediaType.APPLICATION_JSON).content(tmrDtoJson)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(tmrId) ))
                .andExpect(jsonPath("$.outPutCompareRegister",is(128) ))
                .andDo(print());

    }

    @Test
    public void updateTimerFlag() {
    }

    @Test
    public void getTimer() throws Exception{
        createTimers();
        int tmrId=timerRepository.findAll().get(0).getId();
        String uri="/timer/"+tmrId+"?inUse=true";
        mockMvc.perform(get(uri)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(tmrId) ))
                .andExpect(jsonPath("$.outPutCompareRegister",is(240) ))
                .andDo(print());

    }

    private void createTimers()
    {
        Timer tmr1=new Timer();
        tmr1.setInUse(true);
        tmr1.setMode(TimerMode.PHASE_CORRECT_PWM);
        tmr1.setName("timer1");
        tmr1.setOutPutCompareRegister(240);

        Timer tmr2=new Timer();
        tmr2.setInUse(true);
        tmr2.setMode(TimerMode.PHASE_CORRECT_PWM);
        tmr2.setName("timer2");
        tmr2.setOutPutCompareRegister(50);
        timerRepository.save(tmr1);
        timerRepository.save(tmr2);


        /*
        Atmega atmega1= MccFactory.createMcc("0xAA", AtmegaType.ATMEGA18,"atmega1");
        Atmega atmega2=MccFactory.createMcc("0xAB", AtmegaType.ATMEGA18,"atmega2");
        mccService.createMcu(atmega1);
        mccService.createMcu(atmega2);
        */
    }

}