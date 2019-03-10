package com.farmtec.io.message;

import java.net.Socket;
import java.util.Map;

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
    protected byte buffer[];


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


    public abstract Map<String,Integer> decodeToMap();
    public  abstract int addBytes(byte[] inBytes,int bytesRead,int srcStartPost);

}
