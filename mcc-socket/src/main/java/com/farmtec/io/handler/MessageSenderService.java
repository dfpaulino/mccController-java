package com.farmtec.io.handler;

import com.farmtec.io.message.Message;
import com.farmtec.io.ph.Connection;

import java.net.Socket;

public interface MessageSenderService {

    public boolean sendMessage(Message message,String ipAddr);
    public boolean sendMessage(Message message, Connection cnx);
    public boolean sendMessage(Message message, Socket clientSocket);
}
