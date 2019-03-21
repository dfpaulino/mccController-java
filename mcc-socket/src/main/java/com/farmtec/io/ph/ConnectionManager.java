package com.farmtec.io.ph;

import com.farmtec.io.handler.MessageHandlerService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Value("${io.service.max.connections:5}")
     int MAX_CNX;
    @Value("${io.service.connection.monitor.poll.interval:5000}")
    int CNX_MONITOR_POLL_INTERVAL;


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
    @PostConstruct
    public void init(){
        connectionManagerExecutorService = Executors.newFixedThreadPool(MAX_CNX+2);
        this.submitTaskToPool(this);
    }
    //Bean destroy
    public void close(){
        logger.info("Shutting down clients connection Execution pool...");
        connectionManagerExecutorService.shutdown();
        connectionArrayList.clear();
    }

    public List<Connection> getConnectionList(){return this.connectionArrayList;}

    public void submitTaskToPool(Runnable task){this.connectionManagerExecutorService.execute(task);}

    public void createClientSocket(Socket clientSock) throws IOException
    {
        logger.info("Processing connection for client "+clientSock.getInetAddress().toString());

        if (connectionManagerExecutorService instanceof ThreadPoolExecutor) {
            int currentActiveThr=((ThreadPoolExecutor) connectionManagerExecutorService).getActiveCount();
            logger.info("current Threads in connection-pool is =>"+ currentActiveThr+"");
            if(connectionArrayList.size()>=MAX_CNX){
                logger.error("Cant accept more connections...MAX CONNECTIONS reached");
            }else{
                Connection connection= new Connection(clientSock,messageHandlerService);
                if(connectionArrayList.add(connection))
                {
                    logger.info("connection sucessfully created for client "+clientSock.getInetAddress().toString());
                    connectionManagerExecutorService.execute(connection);
                }
            }
        }
    }

    public void monitorConnectionList()
    {
        if(connectionArrayList.size()>0) {
            for (int i = 0; i < connectionArrayList.size(); i++) {
                if (ConnectionsStatus.ACTIVE != connectionArrayList.get(i).getConnectionsStatus()) {
                    logger.warn("removing connection client [" + connectionArrayList.get(i).getClientSocket().toString() + "]from list");

                    connectionArrayList.remove(i);

                    /**
                     * TODO
                     * gather info for stats!!
                     */
                } else {
                    logger.info("connection " + connectionArrayList.get(i).getClientSocket().toString() + " still active");
                }
            }
        }else{
            logger.warn("No connections available..." );
        }

    }

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(CNX_MONITOR_POLL_INTERVAL);
                logger.info("Monitoring Task kicking in...");
                monitorConnectionList();
            }
        }catch (Exception e){

        }
    }
}
