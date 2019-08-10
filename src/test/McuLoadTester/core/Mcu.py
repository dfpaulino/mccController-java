#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Fri May 31 09:38:35 2019

@author: dpaulino
"""

class Mcu:
    model = 'Atmega32'
    
    def __init__(self,name,address):
        self.name=name
        self.address=address
        self.buffer=bytearray(38)
        self.initBuffer()
        
        
    def initBuffer(self):
        self.buffer[0]=0x01
        self.buffer[1]=0x80
        self.buffer[2]=len(self.buffer)
        self.buffer[3]=0x81
        self.buffer[4]=0x01
        self.buffer[5]=0x82
        self.buffer[6]=int(self.address,16)
        self.buffer[7]=0x83
        self.buffer[8]=0
        self.buffer[9]=0x84
        self.buffer[10]=0
        self.buffer[11]=0x85
        self.buffer[12]=0
        self.buffer[13]=0x86
        self.buffer[14]=0
        self.buffer[15]=0x87
        self.buffer[16]=0
        self.buffer[17]=0x88
        self.buffer[18]=0
        self.buffer[19]=0x89
        self.buffer[20]=0
        self.buffer[21]=0x8a
        self.buffer[22]=0
        self.buffer[23]=0x8b
        self.buffer[24]=0
        self.buffer[25]=0x8c
        self.buffer[26]=0
        self.buffer[27]=0x8d
        self.buffer[28]=0
        self.buffer[29]=0x8e
        self.buffer[30]=0
        self.buffer[31]=0x8f
        self.buffer[32]=0
        self.buffer[33]=0x90
        self.buffer[34]=0
        self.buffer[35]=0x91
        self.buffer[36]=0
        self.buffer[37]=0x00

    def incrementBufferValues(self):
        self.buffer[8]=0        
        self.buffer[10]+=1
        self.buffer[12]+=1
        self.buffer[14]+=1
        self.buffer[16]+=1
        self.buffer[18]+=1
        self.buffer[20]+=1
        self.buffer[22]+=1
        self.buffer[24]+=1
        self.buffer[26]+=1
        self.buffer[28]+=1
        self.buffer[30]+=1
        self.buffer[32]+=1
        self.buffer[34]+=1
        self.buffer[36]+=1
        
    def getBuffer(self):
        return self.buffer

        
        
        
        
        