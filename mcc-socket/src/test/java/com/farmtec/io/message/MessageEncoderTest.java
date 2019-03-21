package com.farmtec.io.message;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MessageEncoderTest {


    /**
     *
     *  protected void loadTagEncoderMap(){
     *         tagEncoder = Stream.of(new Object[][] {
     *                 { "length", 0x80 },{ "operation",0x81 },
     *                 { "address",0x82 },{ "timer0",0x83 },
     *                 { "timer1",0x84 },{ "timer2",0x85 },
     *                 { "portA",0x86 },{ "portB",0x87 },
     *                 { "portC",0x88 },{ "portD",0x89 },
     *                 { "adc0",0x8a },{ "adc1",0x8b },
     *                 { "adc2",0x8c },{ "adc3",0x8d },
     *                 { "adc4",0x8e },{ "adc5",0x8f },
     *                 { "adc6",0x90 },{ "adc7",0x91 },
     */
    @Test
    public void encodeMessage() {
        Message encodedMessage=new MessageEncoder(null);
        Map<String,Integer> map=new HashMap<String,Integer>();
        byte addr=0x0a;
        map.put("length",100);//wrong value, but Encoder should ne clever
        map.put("address",(int)0xff);//discarded
        map.put("timer0",0x00);
        map.put("timer1",0x00);
        map.put("timer2",0x00);
        map.put("portA",0xf1);
        map.put("portB",0xf2);
        map.put("portC",0xf3);
        map.put("portD",0xf4);
        map.put("adc0",0x01);
        map.put("adc1",0x02);
        map.put("adc3",0x03);
        encodedMessage.encodeMessage((byte)addr,(byte)0x01,map);
        int expectedMsgSize=MessageEncoder.FRAME_HEADER_SIZE+MessageEncoder.FRAME_FOOTER_SIZE+20;
        assertEquals(encodedMessage.getLength(),expectedMsgSize);
        assertEquals(encodedMessage.buffer[2],expectedMsgSize);
        //verify the frame contents
        assertThat(encodedMessage.buffer[0],is((byte)0x01));
        for (int i =1;i<encodedMessage.buffer.length-1;i+=2)
        {
            System.out.println("\ntag ["+(0xff&encodedMessage.buffer[i])+"] value ["+(0xff&encodedMessage.buffer[i+1])+"]");
            if(encodedMessage.buffer[i]==(byte)0x80)
                assertThat(encodedMessage.buffer[i+1],is((byte)expectedMsgSize));
            if(encodedMessage.buffer[i]==(byte)0x81)
                assertThat(encodedMessage.buffer[i+1],is((byte)0x01));
            if(encodedMessage.buffer[i]==(byte)0x82)
                assertThat(encodedMessage.buffer[i+1],is((byte)addr));
            if(encodedMessage.buffer[i]==(byte)0x83)
                assertThat(encodedMessage.buffer[i+1],is((byte)0x00));
            if(encodedMessage.buffer[i]==(byte)0x84)
                assertThat(encodedMessage.buffer[i+1],is((byte)0x00));
            if(encodedMessage.buffer[i]==(byte)0x85)
                assertThat(encodedMessage.buffer[i+1],is((byte)0x00));
            if(encodedMessage.buffer[i]==(byte)0x86)
                assertThat(encodedMessage.buffer[i+1],is((byte)0xf1));
            if(encodedMessage.buffer[i]==(byte)0x87)
                assertThat(encodedMessage.buffer[i+1],is((byte)0xf2));
            if(encodedMessage.buffer[i]==(byte)0x88)
                assertThat(encodedMessage.buffer[i+1],is((byte)0xf3));
            if(encodedMessage.buffer[i]==(byte)0x89)
                assertThat(encodedMessage.buffer[i+1],is((byte)0xf4));
            if(encodedMessage.buffer[i]==(byte)0x8a)
                assertThat(encodedMessage.buffer[i+1],is((byte)0x01));
            if(encodedMessage.buffer[i]==(byte)0x8b)
                assertThat(encodedMessage.buffer[i+1],is((byte)0x02));
            if(encodedMessage.buffer[i]==(byte)0x8d)
                assertThat(encodedMessage.buffer[i+1],is((byte)0x03));
        }
        System.out.println("Buffer:\n["+((MessageEncoder) encodedMessage).printBuffer()+"]");

        /**
         *
         *  protected void loadTagEncoderMap(){
         *         tagEncoder = Stream.of(new Object[][] {
         *                 { "length", 0x80 },{ "operation",0x81 },
         *                 { "address",0x82 },{ "timer0",0x83 },
         *                 { "timer1",0x84 },{ "timer2",0x85 },
         *                 { "portA",0x86 },{ "portB",0x87 },
         *                 { "portC",0x88 },{ "portD",0x89 },
         *                 { "adc0",0x8a },{ "adc1",0x8b },
         *                 { "adc2",0x8c },{ "adc3",0x8d },
         *                 { "adc4",0x8e },{ "adc5",0x8f },
         *                 { "adc6",0x90 },{ "adc7",0x91 },
         */

    }
}