package com.farmtec.io.ph;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@NoArgsConstructor
public class SocketLstnr implements Runnable{

    Logger logger = LoggerFactory.getLogger(SocketLstnr.class);

    @Value("${io.service.port}")
    private int PORT;
    @Value("${io.service.address:0.0.0.0}")
    private String IP_ADDR;

    @Autowired
    ConnectionManager connectionManager;

    /*my socket*/
    private ServerSocket serverSocket;

    @PostConstruct
    public void init(){this.startServer();}

    public void close(){}


    public void startServer()
    {
        logger.info("Starting server "+PORT +" ... ");
        System.out.println("Starting server "+PORT +" ... ");
        try{
            serverSocket = new ServerSocket(PORT,50, InetAddress.getByName(IP_ADDR));
        }catch (IOException ioe)
        {
            logger.error("Cannot listen to PORT "+PORT +" Exit ");
            ioe.printStackTrace();
            System.exit(-1);
        }
        /*accept connections and store the ID,
        Each connection handled by 1 Thread -> Connection
        run that task in a thread...
        */
        connectionManager.submitTaskToPool(this);
    }

    @Override
    public void run() {
        while (true){

            try {
                logger.info("Waiting for incomming Connection...");
                Socket socket = serverSocket.accept();
                logger.info("Creating incomming Connection...");
                connectionManager.createClientSocket(socket);
            }catch (IOException ioe){
                logger.error("something went wrong when client tried to connect");
            }
        }
    }
}
