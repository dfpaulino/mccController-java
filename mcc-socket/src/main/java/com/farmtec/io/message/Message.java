package com.farmtec.io.message;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Message {


    //from src
    private Socket clientSocket;

    protected MessageStatus messageStatus;
    /**
     * TODO
     * for now only process reportUpdate
     * updates are requests from the MCU providing update
     */
    private String service="reportUpdate";

    //with this content
    protected int length;
    //ByteBuffer of the frame
    protected byte buffer[];
    //decoded ByteBuffer into readable format (tag names)
    protected Map<String,Integer> decodedMessage;

    //Maps to decode and encode the Tags
    protected   Map<Integer,String> tagDecoder=new HashMap<Integer,String>();
    protected   Map<String,Integer> tagEncoder=new HashMap<String,Integer>();



    public Message(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public int getLength() {
        return length;
    }

    public byte[] getBytes() {
        return buffer;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }


    @Override
    public String toString() {
        if(decodedMessage==null)
        {
            decodeToMap();
        }
        return this.decodedMessage.toString();
    }

    protected void loadTagEncoderMap(){
        tagEncoder = Stream.of(new Object[][] {
                { "length", 0x80 },{ "operation",0x81 },
                { "address",0x82 },{ "timer0",0x83 },
                { "timer1",0x84 },{ "timer2",0x85 },
                { "portA",0x86 },{ "portB",0x87 },
                { "portC",0x88 },{ "portD",0x89 },
                { "adc0",0x8a },{ "adc1",0x8b },
                { "adc2",0x8c },{ "adc3",0x8d },
                { "adc4",0x8e },{ "adc5",0x8f },
                { "adc6",0x90 },{ "adc7",0x91 },
        }).collect(Collectors.toMap((data -> (String) data[0]), (data -> (Integer) data[1])));
    }

    protected void loadTagDecoderMap(){
        tagDecoder = Stream.of(new Object[][] {
                { "length", 0x80 },{ "operation",0x81 },
                { "address",0x82 },{ "timer0",0x83 },
                { "timer1",0x84 },{ "timer2",0x85 },
                { "portA",0x86 },{ "portB",0x87 },
                { "portC",0x88 },{ "portD",0x89 },
                { "adc0",0x8a },{ "adc1",0x8b },
                { "adc2",0x8c },{ "adc3",0x8d },
                { "adc4",0x8e },{ "adc5",0x8f },
                { "adc6",0x90 },{ "adc7",0x91 },
        }).collect(Collectors.toMap((data -> (Integer) data[1]), (data -> (String) data[0])));
    }


    /**
     *
     * @return
     * Map<String,int>
     *     where String is the tagName
     *     The int is a byte...just for seeing future cases where fields can be > 256
     */

    public Map<String, Integer> decodeToMap() {
        if (this.tagDecoder==null||this.tagDecoder.size()==0){
            this.loadTagDecoderMap();
        }
        if(null==this.decodedMessage){
            this.decodedMessage=new HashMap<String,Integer>();
        }
        for (int i=1;i<this.buffer.length-2;i=i+2){
            if(tagDecoder.containsKey(Integer.valueOf(0xff&this.buffer[i])))
                this.decodedMessage.put(tagDecoder.get(Integer.valueOf(0xff&this.buffer[i])),Integer.valueOf(0xff&this.buffer[i+1]));
        }
        return this.decodedMessage;
    }


    public abstract boolean encodeMessage(byte addr,byte operation,Map<String, Integer> map);
    public  abstract int addBytes(byte[] inBytes,int bytesRead,int srcStartPost);


}
