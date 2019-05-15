package com.farmtec.mcc.cdr.event;

import com.farmtec.mcc.cdr.config.AsynchronousSpringEventsConfig;
import com.farmtec.mcc.cdr.config.EnableCdr;
import com.farmtec.mcc.cdr.dto.Cdr;
import com.farmtec.mcc.cdr.dto.CdrType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AsynchronousSpringEventsConfig.class)
@TestPropertySource("classpath:application.properties")
@Import({ConfigTest.class})
@EnableCdr
public class CdrEventPublisherTest {

    @Autowired
    CdrEventPublisher cdrEventPublisher;

    @Test
    public void generateCdr() {

        Cdr cdr=new Cdr();
        cdr.setAddr("0a");
        cdr.setCdrType(CdrType.UPDATE_INFO_REQUEST);
        cdr.setNow(new Date());
        cdr.setData("data1,data2,data3,data4");
        cdrEventPublisher.generateCdr(this,cdr);
    }


}