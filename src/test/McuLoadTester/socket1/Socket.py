#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Thu Aug  8 18:31:37 2019

@author: daniel
"""
import socket
import time
import threading

class Socket:
    
    clientId = 0
    
    def __init__(self,ip,port):
        self.host=ip
        self.port=port
        self.s=0
        Socket.clientId+=1
        
    def connect(self):
        self.s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        print ('clientId '+str(Socket.clientId) +'connecting to socket...')
        self.s.connect((self.host,self.port))
        
    def sendPayload(self,buffer1):
        self.s.sendall(buffer1)
        print('clientId '+str(Socket.clientId) +'sent data')
        
    def close(self):
        self.s.close()