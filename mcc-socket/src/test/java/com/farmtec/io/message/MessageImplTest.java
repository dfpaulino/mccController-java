package com.farmtec.io.message;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageImplTest {
    private byte[] buffer1;
    private byte[] buffer2;
    private byte[] buffer3;

    @Before
    public void setUpTestBufers()
    {
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
         * 0x9a  -> ADC7  (read only) (1 byte)
         * 0x00  -> END of Packet
         */
        //Buffer 1 has 2 complete mesages
        buffer1=new byte[36];
        buffer1[0]=0x01;                        //initPack
        buffer1[1]=(byte)0x80;buffer1[2]=18;    // length
        buffer1[3]=(byte)0x81;buffer1[4]=0x01;  //operation
        buffer1[5]=(byte)0x82;buffer1[6]=0x0a;  //address
        buffer1[7]=(byte)0x83;buffer1[8]=(byte)250;  //timer0
        buffer1[9]=(byte)0x84;buffer1[10]=(byte)128;  //timer1
        buffer1[11]=(byte)0x85;buffer1[12]=127;        //timer2
        buffer1[13]=(byte)0x86;buffer1[14]=(byte)0xff; //porta
        buffer1[15]=(byte)0x87;buffer1[16]=(byte)0xaf; //portb
        buffer1[17]=0x00;

        buffer1[18]=0x01;                        //initPack
        buffer1[19]=(byte)0x80;buffer1[20]=18;    // length
        buffer1[21]=(byte)0x81;buffer1[22]=0x01;  //operation
        buffer1[23]=(byte)0x82;buffer1[24]=0x0b;  //address
        buffer1[25]=(byte)0x83;buffer1[26]=(byte)200;  //timer0
        buffer1[27]=(byte)0x84;buffer1[28]=(byte)140;  //timer1
        buffer1[29]=(byte)0x85;buffer1[30]=127;        //timer2
        buffer1[31]=(byte)0x86;buffer1[32]=0x0f; //porta
        buffer1[33]=(byte)0x87;buffer1[34]=0x0f; //portb
        buffer1[35]=0x00;

        //buffer 2 has 1 and half mesage
        buffer2=new byte[19];
        buffer2[0]=0x01;                        //initPack
        buffer2[1]=(byte)0x80;buffer2[2]=12;    // length
        buffer2[3]=(byte)0x81;buffer2[4]=0x01;  //operation
        buffer2[5]=(byte)0x82;buffer2[6]=0x0a;  //address
        buffer2[7]=(byte)0x83;buffer2[8]=(byte)250;  //timer0
        buffer2[9]=(byte)0x86;buffer2[10]=(byte)0xff; //porta
        buffer2[11]=0x00;
        //incomplete mesage
        buffer2[12]=0x01;                        //initPack
        buffer2[13]=(byte)0x80;buffer2[14]=12;    // length
        buffer2[15]=(byte)0x81;buffer2[16]=0x01;  //operation
        buffer2[17]=(byte)0x82;buffer2[18]=0x0a;  //address
        //buffer 3 completes the previous message + 1 full message
        buffer3=new byte[17];
        buffer3[0]=(byte)0x83;buffer3[1]=(byte)250;  //timer0
        buffer3[2]=(byte)0x86;buffer3[3]=(byte)0xff; //porta
        buffer3[4]=0x00;

        buffer3[5]=0x01;                        //initPack
        buffer3[6]=(byte)0x80;buffer3[7]=12;    // length
        buffer3[8]=(byte)0x81;buffer3[9]=0x01;  //operation
        buffer3[10]=(byte)0x82;buffer3[11]=0x0a;  //address
        buffer3[12]=(byte)0x83;buffer3[13]=(byte)250;  //timer0
        buffer3[14]=(byte)0x86;buffer3[15]=(byte)0xff; //porta
        buffer3[16]=0x00;



    }
    //@Test
    public void decodeToMap() {
    }

    @Test
    public void addBytesRead2FullPackets() {
        int nextPos=0;
        Message packet1=new MessageImpl(null);
        nextPos=packet1.addBytes(buffer1,buffer1.length,nextPos);
        assertEquals(18,nextPos);
        assertEquals(MessageStatus.COMPLETE_EXCEDED.getValue(),packet1.getMessageStatus().getValue());

        Message packet2=new MessageImpl(null);
        nextPos=packet2.addBytes(buffer1,buffer1.length,nextPos);
        assertEquals(-1,nextPos);
        assertEquals(MessageStatus.COMPLETE.getValue(),packet2.getMessageStatus().getValue());
    }

    @Test
    public void addBytesRead1AndHalfPackets(){
        int nextPos=0;
        Message packet1=new MessageImpl(null);
        nextPos=packet1.addBytes(buffer2,buffer2.length,nextPos);
        assertEquals(12,nextPos);
        assertEquals(MessageStatus.COMPLETE_EXCEDED.getValue(),packet1.getMessageStatus().getValue());
        assertEquals(12,packet1.getLength());
        assertEquals(0x00,packet1.getBytes()[11]);

        Message packet2=new MessageImpl(null);
        nextPos=packet2.addBytes(buffer2,buffer2.length,nextPos);
        assertEquals(-1,nextPos);
        assertEquals(MessageStatus.INCOMPLETE.getValue(),packet2.getMessageStatus().getValue());
        //next buffer has the rest of the packet
        nextPos=packet2.addBytes(buffer3,buffer3.length,0);
        assertEquals(5,nextPos);
        assertEquals(MessageStatus.COMPLETE_EXCEDED.getValue(),packet1.getMessageStatus().getValue());
        assertEquals(12,packet2.getLength());
        assertEquals(0x00,packet2.getBytes()[11]);
    }

    @Test
    public void addBytes1packPlusNextPack1Byte(){
        byte[] buffer4=new byte[13];
        byte[] buffer5=new byte[11];
        buffer4[0]=0x01;                        //initPack
        buffer4[1]=(byte)0x80;buffer4[2]=12;    // length
        buffer4[3]=(byte)0x81;buffer4[4]=0x01;  //operation
        buffer4[5]=(byte)0x82;buffer4[6]=0x0a;  //address
        buffer4[7]=(byte)0x83;buffer4[8]=(byte)250;  //timer0
        buffer4[9]=(byte)0x86;buffer4[10]=(byte)0xff; //porta
        buffer4[11]=0x00;
        buffer4[12]=0x01; //init of next packet
        //remaining 11 bytes...
        buffer5[0]=(byte)0x80;buffer5[1]=12;    // length
        buffer5[2]=(byte)0x81;buffer5[3]=0x01;  //operation
        buffer5[4]=(byte)0x82;buffer5[5]=0x0a;  //address
        buffer5[6]=(byte)0x83;buffer5[7]=(byte)250;  //timer0
        buffer5[8]=(byte)0x86;buffer5[9]=(byte)0xff; //porta
        buffer5[10]=0x00;

        int nextPos=0;
        Message packet1=new MessageImpl(null);
        nextPos=packet1.addBytes(buffer4,buffer4.length,nextPos);
        assertEquals(12,nextPos);
        assertEquals(MessageStatus.COMPLETE_EXCEDED.getValue(),packet1.getMessageStatus().getValue());
        assertEquals(12,packet1.getLength());
        assertEquals(0x00,packet1.getBytes()[11]);

        Message packet2=new MessageImpl(null);
        nextPos=packet2.addBytes(buffer4,buffer4.length,nextPos);
        assertEquals(-1,nextPos);
        assertEquals(MessageStatus.INCOMPLETE.getValue(),packet2.getMessageStatus().getValue());

        //next buffer has the rest of the packet
        nextPos=packet2.addBytes(buffer5,buffer5.length,0);
        assertEquals(-1,nextPos);
        assertEquals(MessageStatus.COMPLETE.getValue(),packet2.getMessageStatus().getValue());
        assertEquals(12,packet2.getLength());
        assertEquals(0x00,packet2.getBytes()[11]);
    }

    @Test
    public void addBytes1packPlusNextPack2Byte(){
        byte[] buffer4=new byte[14];
        byte[] buffer5=new byte[10];
        buffer4[0]=0x01;                        //initPack
        buffer4[1]=(byte)0x80;buffer4[2]=12;    // length
        buffer4[3]=(byte)0x81;buffer4[4]=0x01;  //operation
        buffer4[5]=(byte)0x82;buffer4[6]=0x0a;  //address
        buffer4[7]=(byte)0x83;buffer4[8]=(byte)250;  //timer0
        buffer4[9]=(byte)0x86;buffer4[10]=(byte)0xff; //porta
        buffer4[11]=0x00;
        buffer4[12]=0x01; //init of next packet
        buffer4[13]=(byte)0x80;//next pack length tag
        //remaining 11 bytes...
        buffer5[0]=12;    // length
        buffer5[1]=(byte)0x81;buffer5[2]=0x01;  //operation
        buffer5[3]=(byte)0x82;buffer5[4]=0x0a;  //address
        buffer5[5]=(byte)0x83;buffer5[6]=(byte)250;  //timer0
        buffer5[7]=(byte)0x86;buffer5[8]=(byte)0xff; //porta
        buffer5[9]=0x00;

        int nextPos=0;
        Message packet1=new MessageImpl(null);
        nextPos=packet1.addBytes(buffer4,buffer4.length,nextPos);
        assertEquals(12,nextPos);
        assertEquals(MessageStatus.COMPLETE_EXCEDED.getValue(),packet1.getMessageStatus().getValue());
        assertEquals(12,packet1.getLength());
        assertEquals(0x00,packet1.getBytes()[11]);

        Message packet2=new MessageImpl(null);
        nextPos=packet2.addBytes(buffer4,buffer4.length,nextPos);
        assertEquals(-1,nextPos);
        assertEquals(MessageStatus.INCOMPLETE.getValue(),packet2.getMessageStatus().getValue());

        //next buffer has the rest of the packet
        nextPos=packet2.addBytes(buffer5,buffer5.length,0);
        assertEquals(-1,nextPos);
        assertEquals(MessageStatus.COMPLETE.getValue(),packet2.getMessageStatus().getValue());
        assertEquals(12,packet2.getLength());
        assertEquals(0x00,packet2.getBytes()[11]);
    }
}