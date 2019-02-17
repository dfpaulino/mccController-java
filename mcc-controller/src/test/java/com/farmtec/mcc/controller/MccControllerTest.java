package com.farmtec.mcc.controller;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.AtmegaType;
import com.farmtec.mcc.models.factory.MccFactory;
import com.farmtec.mcc.repositories.MccRepository;
import com.farmtec.mcc.repositories.config.RepoConfig;

import com.farmtec.mcc.service.MccService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class MccControllerTest {

    @Autowired
    MccController mccController;

    @Autowired
    MccService mccService;

    @Autowired
    MccRepository mccRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contexLoads() throws Exception {
        assertThat(mccController).isNotNull();
    }
    @Test
    public void getAllMcc() throws Exception  {
        create2Mcc();
        mockMvc.perform(get("/mcc/all")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))).andDo(print());

    }

    @Test
    public void getMccById() throws Exception{
        create2Mcc();
        List<Atmega> atmegaList=mccRepository.findAll();
        String urlTemplate="/mcc/"+atmegaList.get(0).getId()+"?inUse=false";
        mockMvc.perform(get(urlTemplate)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("atmega1")))
                .andExpect(jsonPath("$.address", is("0xAA")))
                .andExpect(jsonPath("$.timers", hasSize(3)))
                .andExpect(jsonPath("$.ioPort", hasSize(4)))
                .andExpect(jsonPath("$.adcs", hasSize(8)))
                .andDo(print());

    }

    @Test
    public void addMcc() {
    }

    @Test
    public void deleteMccById() throws Exception{
        create2Mcc();
        List<Atmega> atmegaList=mccRepository.findAll();
        String urlTemplate="/mcc/"+atmegaList.get(0).getId()+"?inUse=false";
        mockMvc.perform(delete(urlTemplate)).andExpect(status().isOk());

        assertEquals(1,mccRepository.findAll().size());
    }

    private void create2Mcc()
    {
        Atmega atmega1=MccFactory.createMcc("0xAA", AtmegaType.ATMEGA18,"atmega1");
        Atmega atmega2=MccFactory.createMcc("0xAB", AtmegaType.ATMEGA18,"atmega2");
        mccService.createMcu(atmega1);
        mccService.createMcu(atmega2);
    }
}