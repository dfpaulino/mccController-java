package com.farmtec.mcc.ph;

import com.farmtec.mcc.handler.MessageHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable
{
    private MessageHandlerService messageHandlerService;

    private Socket clientSocket;

    private PrintWriter out;
    private BufferedReader in;

    private ConnectionsStatus connectionsStatus=ConnectionsStatus.NOT_ACTIVE;

    private long bytesIn;
    private long bytesOut;

    private Logger logger = LoggerFactory.getLogger(Connection.class);


    public Connection(Socket clientSocket, MessageHandlerService messageHandlerService) {
        this.clientSocket = clientSocket;

        try{
            in=new BufferedReader(
                    new InputStreamReader(this.clientSocket.getInputStream()));
            out=new PrintWriter(this.clientSocket.getOutputStream(),true);
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

    public PrintWriter getOut() {
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

    @Override
    public void run() {
        logger.info("Connection ["+this.clientSocket.toString()+"] starting to process incomming packets...");

    }
}
