package com.farmtec.mcc.handler;

import com.farmtec.mcc.message.Message;

public interface Handler {
    public void execute(Message msg);
}
