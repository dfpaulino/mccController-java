package com.farmtec.mcc.service;

import com.farmtec.io.config.EnableIO;
import com.farmtec.io.message.Message;
import com.farmtec.io.message.MessageImpl;
import com.farmtec.io.message.MessageStatus;
import com.farmtec.mcc.config.ServiceIOConfig;
import com.farmtec.mcc.dto.modules.AtmegaDto;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.AtmegaType;
import com.farmtec.mcc.models.factory.MccFactory;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.utils.Util;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@Rollback(true)
//@EnableAutoConfiguration
@RunWith(SpringRunner.class)
//@DataJpaTest
@SpringBootTest(classes = ServiceIoReceiver.class)
@EnableTransactionManagement
@TestPropertySource("classpath:application.properties")
//@Import({RepoConfig.class, ServiceIOConfig.class})
@Import({ServiceIOConfig.class})
@EnableIO
public class ServiceIOTest {

    @Autowired
    MccService mccService;

    @Autowired
    ServiceIoReceiver serviceIoReceiver;

    @Autowired
    ServiceIoSender serviceIoSender;


    byte[] message=new byte[]{0x01,};

    @Before()
    public void setUp() throws Exception {

    }

    @Test
    public void sendMessage()
    {
        AtmegaDto mccDto=new AtmegaDto();
        mccDto.setAddress("0a");
        List<TimerDto> timers=new ArrayList<TimerDto>();
        TimerDto timer=new TimerDto();
        timer.setName("timer0");
        timer.setOutPutCompareRegister(0x80);
        timers.add(timer);
        mccDto.setTimers(timers);
        serviceIoSender.sendMessage(mccDto,Operation.UpdateMcu);
    }

    @Test
    public void execute() {
        create2Mcu();
        Message msg=buildMessage("0a");
        serviceIoReceiver.execute(msg);

        assertEquals(MessageStatus.COMPLETE,msg.getMessageStatus());
        /*Lets validate some value...
        buffer1[21]=(byte)0x8a;buffer1[22]=(byte)0xff; //adc0
        buffer1[23]=(byte)0x8b;buffer1[24]=(byte)0xaf; //adc1
        buffer1[25]=(byte)0x8c;buffer1[26]=(byte)0x0a; //adc2
        buffer1[27]=(byte)0x8d;buffer1[28]=(byte)0x00; //adc3
        buffer1[29]=(byte)0x8e;buffer1[30]=(byte)0x00; //adc4
        buffer1[31]=(byte)0x8f;buffer1[32]=(byte)0x00; //adc5
        buffer1[33]=(byte)0x90;buffer1[34]=(byte)0x00; //adc6
        buffer1[35]=(byte)0x91;buffer1[36]=(byte)0x00; //adc7
        */
        Atmega mcu=mccService.getMcuDetailsByAddress("0a");
        List<ADC> adcList=mcu.getAdcs();
        boolean fullMatch=false;
        for (ADC adc:adcList) {
            String key="adc"+adc.getAdcId();
            if(adc.getValue()==(int)msg.decodeToMap().get(key)){
                fullMatch=true;
                System.out.println("ADC "+adc.getAdcId()+" Match value "+msg.decodeToMap().get(key));
            }
            if(fullMatch==false){break;}
        }
        assertEquals(true,fullMatch);



        List<Timer> timerList=mcu.getTimers();
         fullMatch=false;
        for (Timer timer:timerList) {

            if(timer.getOutPutCompareRegister()==(int)msg.decodeToMap().get(timer.getName())){
                fullMatch=true;
                System.out.println("Timer "+timer.getName()+" Match value "+msg.decodeToMap().get(timer.getName()));
            }
            if(fullMatch==false){break;}
        }
        assertEquals(true,fullMatch);

        List<PORTn> portList=mcu.getIoPort();
        fullMatch=false;
        for (PORTn port:portList) {

            int x =0xff&port.getValue();
            System.out.println("Port "+port.getPortName()+" compare value "+x+" with Message value"+msg.decodeToMap().get(port.getPortName()));
            if(((int)0xff&port.getValue())==(0xff&msg.decodeToMap().get(port.getPortName()))){
                fullMatch=true;
                System.out.println("PORT "+port.getPortName()+" Match value "+ Util.IntegerToByteReadableHex(msg.decodeToMap().get(port.getPortName())));
            }
            if(fullMatch==false){break;}
        }
        assertEquals(true,fullMatch);
    }

    private void create2Mcu(){
        Atmega mcu= MccFactory.createMcc("0a",AtmegaType.ATMEGA32,"ATMEGA1");
        Atmega mcu2= MccFactory.createMcc("fa",AtmegaType.ATMEGA32,"ATMEGA1");
        mccService.createMcu(mcu);
        mccService.createMcu(mcu2);


    }

    private Message buildMessage(String address){
        byte[] buffer1;
        /**
         * Packet :
         * 0x01 -> init of protocol
         * 0x80  -> length tag  (1 Byte))
         * 0x81  -> Operation (Read Response|Write Request)
         * 0x82  -> address (1 bytes)
         * 0x83  -> timer0 value (R/W) (1 byte) (R/W)
         * 0x84  -> timer1 value (R/W) (1 byte)
         * 0x85  -> timer2 value (R/W) (1 byte)
         * 0x86  -> PORTA (R/W) (1 byte)
         * 0x87  -> PORTB (R/W) (1 byte)
         * 0x88  -> PORTC (R/W) (1 byte)
         * 0x89  -> PORTD (R/W) (1 byte)
         * 0x8a  -> ADC0  (read only) (1 byte)
         * 0x8b  -> ADC1  (read only) (1 byte)
         * 0x8c  -> ADC2  (read only) (1 byte)
         * 0x8d  -> ADC3  (read only) (1 byte)
         * 0x8e  -> ADC4  (read only) (1 byte)
         * 0x8f  -> ADC5  (read only) (1 byte)
         * 0x90  -> ADC6  (read only) (1 byte)
         * 0x91  -> ADC7  (read only) (1 byte)
         * 0x00  -> END of Packet
         */
        byte addr=Byte.valueOf(address,16).byteValue();
        //Buffer 1 has 2 complete mesages
        buffer1=new byte[38];
        buffer1[0]=0x01;                        //initPack
        buffer1[1]=(byte)0x80;buffer1[2]=38;    // length
        buffer1[3]=(byte)0x81;buffer1[4]=Operation.ReportUpdate.getValue();  //operation
        buffer1[5]=(byte)0x82;buffer1[6]=0x0a;  //address
        buffer1[7]=(byte)0x83;buffer1[8]=(byte)250;  //timer0
        buffer1[9]=(byte)0x84;buffer1[10]=(byte)128;  //timer1
        buffer1[11]=(byte)0x85;buffer1[12]=127;        //timer2
        buffer1[13]=(byte)0x86;buffer1[14]=(byte)0xff; //porta
        buffer1[15]=(byte)0x87;buffer1[16]=(byte)0xaf; //portb
        buffer1[17]=(byte)0x88;buffer1[18]=(byte)0x0a; //portc
        buffer1[19]=(byte)0x89;buffer1[20]=(byte)0x00; //portd

        buffer1[21]=(byte)0x8a;buffer1[22]=(byte)0xff; //adc0
        buffer1[23]=(byte)0x8b;buffer1[24]=(byte)0xaf; //adc1
        buffer1[25]=(byte)0x8c;buffer1[26]=(byte)0x0a; //adc2
        buffer1[27]=(byte)0x8d;buffer1[28]=(byte)0x00; //adc3
        buffer1[29]=(byte)0x8e;buffer1[30]=(byte)0x00; //adc4
        buffer1[31]=(byte)0x8f;buffer1[32]=(byte)0x00; //adc5
        buffer1[33]=(byte)0x90;buffer1[34]=(byte)0x00; //adc6
        buffer1[35]=(byte)0x91;buffer1[36]=(byte)0x00; //adc7
        buffer1[37]=0x00;
        Message message=new MessageImpl(null);
        int x=message.addBytes(buffer1,buffer1.length,0);
        return message;
    }
}