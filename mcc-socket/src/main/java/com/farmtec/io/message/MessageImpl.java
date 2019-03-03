package com.farmtec.io.message;

import java.net.Socket;
import java.util.Map;

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
public class MessageImpl extends Message {

    private static final byte PROTO = 0x01;

    private byte[] tmp = new byte[] {0x00,0x00};

    private int nextAvailablePosInBufer=0;

    public MessageImpl(Socket clientSocket) {
        super(clientSocket);
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public Map<String, byte[]> decodeToMap() {
        return null;
    }

    /**
     *
     * @param inBytes: bufferArray
     * @param bytesRead : bytes read into the bufferArray
     * @param srcStartPost: offset. position to start reading bytes
     * @return next readable position from bufferArray
     * -1: array was 100% read
     * >0 array was not fully read...iterate again on srcStartPos
     */
    @Override
    public int addBytes(byte[] inBytes,int bytesRead, int srcStartPost) {

        int returnNextReadPosition=0;
        //Starting a message
        if (null == this.buffer) {
            //beginning of new packet
            if (0x01 == inBytes[srcStartPost]) {

                int bytesAvailable = bytesRead - srcStartPost ;

                if (bytesAvailable < 3) {
                    //not enough info to create the buffer...lets wait for more bytes...
                    System.arraycopy(inBytes, srcStartPost, tmp, 0, bytesAvailable);
                    this.messageStatus=MessageStatus.INCOMPLETE;
                    returnNextReadPosition=-1;
                    return -1;
                }else {
                    //ok...we have at least the packet length
                    int packetLength = (int) inBytes[srcStartPost + 2];
                    this.length=packetLength;
                    this.buffer = new byte[packetLength];
                    //lets copy some bytes....
                    if(bytesAvailable==packetLength){
                        System.arraycopy(inBytes, srcStartPost, this.buffer, 0, packetLength);
                        nextAvailablePosInBufer += packetLength;
                        this.messageStatus=MessageStatus.COMPLETE;
                        returnNextReadPosition=-1;
                        return -1;
                    }else if(bytesAvailable>packetLength){
                        System.arraycopy(inBytes, srcStartPost, this.buffer, 0, packetLength);
                        nextAvailablePosInBufer += packetLength;
                        this.messageStatus=MessageStatus.COMPLETE_EXCEDED;
                        returnNextReadPosition=srcStartPost+packetLength;
                        return srcStartPost+packetLength;
                    }else if(bytesAvailable<packetLength){
                        System.arraycopy(inBytes, srcStartPost, this.buffer, 0, bytesAvailable);
                        nextAvailablePosInBufer += bytesAvailable;
                        this.messageStatus=MessageStatus.INCOMPLETE;
                        returnNextReadPosition=-1;
                        return -1;
                    }
                }
            } else{
                if (0x01==tmp[0]){
                    System.out.println(this.getClass().getName()+": scenario 1!!");
                    int packetLength;
                    //buffer still 0... its not a protocol Init... then it can only be that last read was < 3 bytes
                    //copy the tmp into buffer
                    if(tmp[1]!=0x00){
                        System.out.println(this.getClass().getName()+": scenario 1.1!!");

                        packetLength = (int) inBytes[srcStartPost];
                        //copy the inBytes
                        this.buffer = new byte[packetLength];
                        this.length=packetLength;
                        this.buffer[0]=tmp[0];
                        this.buffer[1]=tmp[1];
                        int dstPos=2;
                        System.arraycopy(inBytes, srcStartPost, this.buffer, dstPos, (packetLength-2));
                        if(bytesRead==(packetLength-2)){
                            this.messageStatus=MessageStatus.COMPLETE;
                            returnNextReadPosition=-1;
                            return -1;
                        }else if (bytesRead>(packetLength-2)){
                            this.messageStatus=MessageStatus.COMPLETE_EXCEDED;
                            returnNextReadPosition=srcStartPost+(packetLength-2);
                            return srcStartPost+(packetLength-2);
                        }

                    }else{
                        System.out.println(this.getClass().getName()+": scenario 1.2!!");
                        packetLength = (int) inBytes[srcStartPost+1];
                        this.length=packetLength;
                        this.buffer = new byte[packetLength];
                        this.buffer[0]=tmp[0];
                        int dstPos=1;
                        System.arraycopy(inBytes, srcStartPost, this.buffer, dstPos, packetLength-1);
                        if(bytesRead==(packetLength-1)){
                            this.messageStatus=MessageStatus.COMPLETE;
                            returnNextReadPosition=-1;
                            return -1;
                        }else if (bytesRead>(packetLength-1)){
                            this.messageStatus=MessageStatus.COMPLETE_EXCEDED;
                            returnNextReadPosition=srcStartPost+(packetLength-1);
                            return srcStartPost+(packetLength-1);
                        }
                    }
                }else{
                    System.out.println(this.getClass().getName()+": Bytes out of order!!");
                }
            }
        } else {
            //buffer is not complete yet
            // packLen=8
            //nextAvailPos=5 (5 bytes already read (0 to 4))
            //need to read 4
            int bytsToCompletepack=this.length-nextAvailablePosInBufer;
            System.arraycopy(inBytes, srcStartPost, this.buffer, nextAvailablePosInBufer, bytsToCompletepack);
            if(bytesRead==bytsToCompletepack){
                this.messageStatus=MessageStatus.COMPLETE;
                returnNextReadPosition=-1;
                return -1;
            }else if(bytesRead>bytsToCompletepack){
                this.messageStatus=MessageStatus.COMPLETE_EXCEDED;
                returnNextReadPosition=srcStartPost+bytsToCompletepack;
                return srcStartPost+bytsToCompletepack;
            }
        }
        return returnNextReadPosition;
    }
}
