package com.farmtec.io.handler;

import com.farmtec.io.message.Message;

public interface Handler {
    public void execute(Message msg);
}
