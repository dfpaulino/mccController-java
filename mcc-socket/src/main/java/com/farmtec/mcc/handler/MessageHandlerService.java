package com.farmtec.mcc.handler;

import com.farmtec.mcc.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component
public class MessageHandlerService{

    @Value("${service.message.handler.pool.size:2}")
    private int POOL_SIZE;

    /**
     * My MessageHandlers will process the Message
     * Handlers must be register so that the message can be processed
     */
    private Map<String,Handler> messageHandlersRegister=new ConcurrentHashMap<String, Handler>(1);
    private ExecutorService mesageHandlerPool;


}
