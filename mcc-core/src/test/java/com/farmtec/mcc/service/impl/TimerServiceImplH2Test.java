package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import com.farmtec.mcc.repositories.TimerRepository;
import com.farmtec.mcc.repositories.config.RepoConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimerServiceImpl.class)
@Rollback(true)
@EnableAutoConfiguration
@EnableTransactionManagement
@TestPropertySource("classpath:application.properties")
@Import(RepoConfig.class)
@Sql(scripts = {"classpath:data-h2.sql"})
public class TimerServiceImplH2Test {


    @Autowired
    private TimerServiceImpl timerService;

    @Autowired
    private TimerRepository timerRepository;

    @Before
    public void setUp()
    {

    }

    @Test

    public void updateTimerById() {

        List<Timer> timerList=timerRepository.findAll();

        TimerDto timerDto=new TimerDto();

        timerDto.setId(timerList.get(0).getId());

        timerDto.setOutPutCompareRegister(201);
        //timerDto.setInUse(true);
        //timerDto.setMode(TimerMode.PHASE_CORRECT_PWM);
        timerDto.setName("timer0");
        Timer myTmr=timerService.updateTimerById(timerDto);
        assertEquals(timerDto.getOutPutCompareRegister(),timerRepository.getOne(timerDto.getId()).getOutPutCompareRegister());
        //assertEquals(timerDto.getMode().getValue(),myTmr.getMode().getValue());
        //assertEquals(timerRepository.getOne(timerDto.getId()).getMode().getValue(),myTmr.getMode().getValue());
    }

    @Test
    public void updateTimerByIdWithMode() {

        List<Timer> timerList=timerRepository.findAll();

        TimerDto timerDto=new TimerDto();

        timerDto.setId(timerList.get(0).getId());

        //timerDto.setOutPutCompareRegister(150);
        //timerDto.setInUse(true);
        timerDto.setMode(TimerMode.PHASE_CORRECT_PWM);
        Timer myTmr=timerService.updateTimerById(timerDto);
        //assertEquals(timerDto.getOutPutCompareRegister(),timerRepository.getOne(timerDto.getId()).getOutPutCompareRegister());
        //assertEquals(timerDto.getMode().getValue(),myTmr.getMode().getValue());
        assertEquals(timerRepository.getOne(timerDto.getId()).getMode().getValue(),myTmr.getMode().getValue());
    }

    @Test
    @Ignore
    public void setInUseFlag() {
    }

    @Test
    @Ignore
    public void setModeById() {
    }

    @Test
    @Ignore
    public void getInfo() {
    }
}