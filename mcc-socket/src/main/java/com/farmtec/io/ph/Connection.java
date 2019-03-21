package com.farmtec.io.ph;

import com.farmtec.io.handler.MessageHandlerService;
import com.farmtec.io.message.Message;
import com.farmtec.io.message.MessageImpl;
import com.farmtec.io.message.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable
{
    private static final int BUFFER_SIZE=1500;
    private byte[] socketInBuffer=new byte[BUFFER_SIZE];

    private MessageHandlerService messageHandlerService;

    private Socket clientSocket;

    private OutputStream out;
    private InputStream in;

    private ConnectionsStatus connectionsStatus=ConnectionsStatus.NOT_ACTIVE;

    private long bytesIn;
    private long bytesOut;

    private Logger logger = LoggerFactory.getLogger(Connection.class);


    public Connection(Socket clientSocket, MessageHandlerService messageHandlerService) {
        this.clientSocket = clientSocket;

        try{
            in=this.clientSocket.getInputStream();
            out=this.clientSocket.getOutputStream();
            this.messageHandlerService=messageHandlerService;

            this.connectionsStatus=ConnectionsStatus.ACTIVE;
            logger.info("created input/output stream for connection ["+clientSocket.getInetAddress().toString()+"]");
        }catch (IOException ioe)
        {
            logger.error("Unable to create input/output stream for connection ["+clientSocket.getInetAddress().toString()+"]");
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public OutputStream getOut() {
        return out;
    }

    public ConnectionsStatus getConnectionsStatus() {
        return connectionsStatus;
    }

    public long getBytesIn() {
        return bytesIn;
    }

    public long getBytesOut() {
        return bytesOut;
    }

    public synchronized boolean  send(byte[] buffer){
        if(logger.isDebugEnabled())
            logger.debug("Sending bytes to socket ["+this.clientSocket.toString()+"]");
        if(logger.isTraceEnabled())
            logger.trace("Sending ["+buffer);
        try{
            this.out.write(buffer);
            this.out.flush();
            this.bytesOut=+buffer.length;
            return true;
        }catch (IOException ioe){
            logger.error("Error sending mesage to "+clientSocket.toString()+" Terminating connection...");
            this.connectionsStatus=ConnectionsStatus.NOT_ACTIVE;
        }
        return false;
    }

    @Override
    public void run() {
        logger.info("Connection ["+this.clientSocket.toString()+"] starting to process incoming packets...");
        //1st message object after connection is established
        Message packet =new MessageImpl(clientSocket);
        int bytesRead;
        while(true){
            try {
                bytesRead=in.read(socketInBuffer);
                if(-1==bytesRead){break;}

                this.bytesIn+=bytesRead;
                int nextSrcPos=packet.addBytes(socketInBuffer,bytesRead,0);
                /*
                 * here we can he 2 options
                 * Packet is COMPLETE
                 *          return -1
                 * Packet is COMPLETE_EXCEDED
                 *       inject the remainin bytes to the new packet
                 *         return >0
                 * Packet is incomplete
                 *         >-1
                 *
                 *-1 means it copied all bytes available
                 */
                if(nextSrcPos==-1){
                    if(MessageStatus.COMPLETE==packet.getMessageStatus()){

                        if(logger.isDebugEnabled()){
                            logger.debug("current packet complete...submitting message to processor");
                        }
                        if(!messageHandlerService.processMessage(packet)){
                            logger.error("Message not processed...");
                        }
                        packet=new MessageImpl(clientSocket);

                    }else if(MessageStatus.INCOMPLETE==packet.getMessageStatus()){
                        if(logger.isDebugEnabled()){
                            logger.debug("current packet not complete...waiting for next read");
                        }
                    }else {
                        //this should not happen...
                        logger.error("packet add bytes returned -1 ...but status is unkown");
                    }
                }else if(nextSrcPos>0 &&MessageStatus.COMPLETE_EXCEDED==packet.getMessageStatus() ){

                    if(logger.isDebugEnabled()){
                        logger.debug("current packet complete exceeded...submitting message to processor");
                    }
                    if(!messageHandlerService.processMessage(packet)){
                        logger.error("Message not processed...");
                    }
                    while(nextSrcPos>0 )
                    {
                        packet=new MessageImpl(clientSocket);
                        nextSrcPos=packet.addBytes(socketInBuffer,bytesRead,nextSrcPos);
                        if(MessageStatus.COMPLETE==packet.getMessageStatus()||MessageStatus.COMPLETE_EXCEDED==packet.getMessageStatus()){
                            if(logger.isDebugEnabled()){
                                logger.debug("current packet status["+packet.getMessageStatus()+"]...submitting message to processor");
                            }
                            if(!messageHandlerService.processMessage(packet)){
                                logger.error("Message not processed...");
                            }
                        }else {
                            logger.debug("Scenario Exceeded, current pack Incomplete...waiting for next read");
                        }
                    }
                }

            }catch (IOException ioe){
                logger.error("Error reading from Client ["+this.clientSocket.toString()+"] closing connections");
                ioe.printStackTrace();
                break;
            }
        }
        //if we get here..connections is over
        this.connectionsStatus=ConnectionsStatus.NOT_ACTIVE;
        logger.warn("Closing connection from "+clientSocket.toString());
        try {
            in.close();
            out.close();
            clientSocket.close();
        }catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
