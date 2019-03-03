package com.farmtec.io.handler;

import com.farmtec.io.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class MessageHandlerService{

    Logger logger = LoggerFactory.getLogger(MessageHandlerService.class);
    @Value("${service.message.handler.pool.size:2}")
    private int POOL_SIZE;

    /**
     * My MessageHandlers will process the Message
     * Handlers must be register so that the message can be processed
     */
    private Map<String,Handler> messageHandlersRegister=new ConcurrentHashMap<String, Handler>(1);
    private ExecutorService mesageHandlerPool;

    @PostConstruct
    public void init(){
        logger.info("Initiating bean "+this.getClass().getName());
        mesageHandlerPool= Executors.newFixedThreadPool(POOL_SIZE);
        this.registerHandler("defaultHandler",new BasicHandler());
    }

    public boolean registerHandler(String serviceName, Handler handler){
        if(!messageHandlersRegister.containsKey(serviceName))
        {
            messageHandlersRegister.put(serviceName,handler);
            logger.info("Handler ["+handler.getClass().toString()+"] registered for service ["+serviceName+"]");
            return true;
        }else{
            logger.error("Handler ["+handler.getClass().toString()+"] not registered for service ["+ serviceName +"]");
            return false;
        }
    }

    public boolean processMessage(Message message){
        Handler handler=messageHandlersRegister.get(message.getService());
        if(null==handler){
            logger.error("no Handler for service "+message.getService());
            handler=messageHandlersRegister.get("defaultHandler");
        }
        MessageHandler messageHandler=new MessageHandler(handler,message);


        return true;
    }

}
