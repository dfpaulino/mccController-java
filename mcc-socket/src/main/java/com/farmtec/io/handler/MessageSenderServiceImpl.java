package com.farmtec.io.handler;

import com.farmtec.io.message.Message;
import com.farmtec.io.message.MessageStatus;
import com.farmtec.io.ph.Connection;
import com.farmtec.io.ph.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.List;

@Service
public class MessageSenderServiceImpl implements MessageSenderService {

    @Autowired
    ConnectionManager connectionManager;

    Logger logger = LoggerFactory.getLogger(MessageSenderServiceImpl.class);


    @Override
    public boolean sendMessage(Message message, String ipAddr) {

        if(null==message.getClientSocket())
        {// we need to search for a connection with the ip or Host
            List<Connection> connectionList=connectionManager.getConnectionList();
            Connection cnx=null;
            if(logger.isDebugEnabled())
                logger.debug("getting connection for "+ipAddr);

            cnx=connectionList.stream().filter(connection -> connection.getClientSocket().getInetAddress().getHostAddress()
                    .equals(ipAddr)).findFirst().orElse(null);
            if(cnx==null){
                logger.error("Cant find any connection for ip "+ipAddr);
                return false;
            }
            if(message.getMessageStatus()== MessageStatus.COMPLETE) {
                logger.info("Sending message to client [" + cnx.getClientSocket().toString() + "]");
                return cnx.send(message.getBytes());
            }
            else{
                logger.error("Message : status is not completed!!");
            }

        }else{
            //TODO
            //we need to seach the connection that
        }
        return false;
    }

    @Override
    public boolean sendMessage(Message message, Connection cnx) {
        return false;
    }

    @Override
    public boolean sendMessage(Message message, Socket clientSocket) {
        return false;
    }
}
