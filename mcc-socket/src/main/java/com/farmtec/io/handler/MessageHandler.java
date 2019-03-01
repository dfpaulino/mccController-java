package com.farmtec.io.handler;

import com.farmtec.io.message.Message;

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
