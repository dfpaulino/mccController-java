package com.farmtec.io.message;

import java.net.Socket;
import java.util.Map;

public class MessageEncoder extends Message {

    //0x01 0x80 (length) 0x81 (address) 0x82 (operation)
    static final byte FRAME_HEADER_SIZE=7;
    static final byte FRAME_FOOTER_SIZE=1;

    public MessageEncoder(Socket clientSocket) {
        super(clientSocket);
    }

    @Override
    public Map<String, Integer> decodeToMap() {
        return null;
    }

    @Override
    public boolean encodeMessage(byte addr,byte operation,Map<String, Integer> map) {
        this.loadTagEncoderMap();
        /*figure out the length of the message
        Each field is 1Byte, then multiply by 2 as we need to add tags (1 byte each)
        Add header and footer
          */
        byte contentLength=0;
        //remove any header info from the map
        map.remove("address");
        map.remove("operation");
        map.remove("length");

        //lets get the contnent length, but using known keys
        for (String key:map.keySet()) {
            if(this.tagEncoder.containsKey(key)){
                contentLength++;
            }
        }

        byte frameLength=(byte)(2*contentLength+FRAME_HEADER_SIZE+ FRAME_FOOTER_SIZE);
        this.buffer=new byte[frameLength];
        //build the header
        this.buffer[0]=0x01;
        this.buffer[1]=(byte)0x80;this.buffer[2]=frameLength;
        this.buffer[3]=(byte)0x81;this.buffer[4]=operation;
        this.buffer[5]=(byte)0x82;this.buffer[6]=addr;

        //current idx position
        int currentBufferIdx=6;

        //encode content...igore the header if there....
        for (String key:map.keySet()) {
            if(this.tagEncoder.containsKey(key)){
                this.buffer[++currentBufferIdx]=(byte) (0xff&tagEncoder.get(key)) ;this.buffer[++currentBufferIdx]=(byte)(0xff&map.get(key));
            }
        }
        //add footer
        this.buffer[++currentBufferIdx]=0x00;
        //set main variables
        this.length=buffer.length;
        this.messageStatus=MessageStatus.COMPLETE;

        //lets validate if the currentBufferIdx reflects the frame length
        if(frameLength==(currentBufferIdx+1)){
            return true;
        }else{
            System.out.println(this.getClass().getName()+"  Error Encoding frame. Expected currentBuffferIdx ["
                    +(frameLength-1)+"] got ["+currentBufferIdx+"]");
            return false;
        }

    }

    @Override
    public int addBytes(byte[] inBytes, int bytesRead, int srcStartPost) {
        return 0;
    }

    public String printBuffer(){

        StringBuilder aux=new StringBuilder("Init Protocol ["+buffer[0]+"]");
        for (int i =1;i<this.buffer.length-1;i+=2)
        {
            aux.append("\ntag ["+(0xff&buffer[i])+"] value ["+(0xff&buffer[i+1])+"]");
        }
        aux.append(" \nEnd ["+buffer[this.buffer.length-1]);
        return aux.toString();
    }
}
