package com.farmtec.mcc.ph;

import com.farmtec.mcc.handler.MessageHandlerService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@NoArgsConstructor
public class ConnectionManager implements  Runnable{
    Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    @Value("${service.max.connections:5}")
     int MAX_CNX;

    @Autowired
    MessageHandlerService messageHandlerService;

    /**
     * Info about the incomming connections
     * each connections has
     * countBytesIn
     * countBytesOut
     * Status: ACTIVE|NOT_ACTIVE
     * etc
     */
    private List<Connection> connectionArrayList =new ArrayList<Connection>();

    /**Thr pool for cnx. Each incomming connection will be managed by 1 thread::Connection
     * An additional thr will be used to monitor the current connection
     */
    private ExecutorService connectionManagerExecutorService;

    //Bean initialization
    public void init(){
        connectionManagerExecutorService = Executors.newFixedThreadPool(MAX_CNX);
        this.submitTaskToPool(this);
    }
    //Bean destroy
    public void close(){
        logger.info("Shutting down clients connection Execution pool...");
        connectionManagerExecutorService.shutdown();
        connectionArrayList.clear();
    }

    public void submitTaskToPool(Runnable task){this.connectionManagerExecutorService.execute(task);}

    public void createClientSocket(Socket clientSock) throws IOException
    {

        logger.info("Processing connection for client "+clientSock.getInetAddress().toString());

        if (connectionManagerExecutorService instanceof ThreadPoolExecutor) {
            int currentActiveThr=((ThreadPoolExecutor) connectionManagerExecutorService).getActiveCount();
            if(currentActiveThr==(MAX_CNX+2)){
                logger.error("Cant accept more connections...MAX CONNECTIONS reached");
            }else{
                Connection connection= new Connection(clientSock,messageHandlerService);
                if(connectionArrayList.add(connection))
                {
                    logger.info("connection sucessfully created for client "+clientSock.getInetAddress().toString());
                }
                connectionManagerExecutorService.execute(connection);
            }
        }
    }

    public void monitorConnectionList()
    {
        for (int i = 0; i< connectionArrayList.size(); i++) {
            if (ConnectionsStatus.ACTIVE != connectionArrayList.get(i).getConnectionsStatus()) {
                logger.warn("removing connection client ["+connectionArrayList.get(i).getClientSocket().toString()+"]from list");
                connectionArrayList.remove(i);

                /**
                 * TODO
                 * gather info for stats!!
                 */
            }
        }

    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
            monitorConnectionList();
        }catch (Exception e){

        }
    }
}
