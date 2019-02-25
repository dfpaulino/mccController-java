package com.farmtec.mcc.handler;

import com.farmtec.mcc.message.Message;

public class MessageHandler implements  Runnable{

    private Handler handler;
    private Message message;

    public MessageHandler(Handler handler, Message message) {
        this.handler = handler;
        this.message = message;
    }

    @Override
    public void run() {
        handler.execute(message);
    }
}
