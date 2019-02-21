package com.farmtec.mcc.controller;

import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.repositories.PortRepository;
import com.farmtec.mcc.service.MccServiceException;
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
public class PortnControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PortnController portnController;

    @Autowired
    PortRepository portRepository;


    @Test
    public void getPortInfo() throws  Exception{
        createPort();
        int portId=portRepository.findAll().get(0).getId();
        String uri="/port/"+portId;
        mockMvc.perform(get(uri)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(portId)))
                .andExpect(jsonPath("$.value",is("0xaf")))
                .andDo(print());

    }

    @Test
    public void updatePortValue() throws  Exception{
        createPort();
        int portId=portRepository.findAll().get(0).getId();
        String uri="/port/"+portId+"/value";
        mockMvc.perform(put(uri).param("value","ff")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(portId)))
                .andExpect(jsonPath("$.value",is("0xff")))
                .andDo(print());
    }

    @Test()
    public void updatePortValueNotExist() throws  Exception{
        createPort();
        int portId=200;
        String uri="/port/"+portId+"/value";
        mockMvc.perform(put(uri).param("value","ff")).andExpect(status().isNotFound())
                .andDo(print());
    }



    private void createPort()
    {
        PORTn porTn=new PORTn();
        porTn.setDdb((byte) 0xff);
        porTn.setPortName("PortA");
        porTn.setValue((byte)0xAF);
        porTn.isInUse();
        portRepository.save(porTn);

    }

}