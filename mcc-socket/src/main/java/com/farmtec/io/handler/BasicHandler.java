package com.farmtec.io.handler;

import com.farmtec.io.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicHandler implements  Handler {

    Logger logger = LoggerFactory.getLogger(BasicHandler.class);

    @Override
    public void execute(Message msg) {
    logger.warn("Using Default Handler... for message ["+msg.toString()+"]");
    }
}
