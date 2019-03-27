# -*- coding: utf-8 -*-
#!/usr/bin/env python3
"""
Spyder Editor

This is a temporary script file.
"""

import socket

host="127.0.0.1"
port=6869

s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
print "connecting to socket..."
s.connect((host,port))

print "sending data..."
buffer1=bytearray(20)

buffer1[0]=0x01  #initPack
buffer1[1]=0x80
buffer1[2]=20    # length
buffer1[3]=0x81
buffer1[4]=0x01  #operation
buffer1[5]=0x82
buffer1[6]=0x0a  #address
buffer1[7]=0x83
buffer1[8]=250  #timer0
buffer1[9]=0x84
buffer1[10]=128  #timer1
buffer1[11]=0x85
buffer1[12]=127        #timer2
buffer1[13]=0x86
buffer1[14]=0x01 #porta
buffer1[15]=0x87
buffer1[16]=0xaf #portb
buffer1[17]=0x8a
buffer1[18]=255 #ad0 0
buffer1[19]=0x00


s.sendall(buffer1)
print "data sent...witing for receive"
data=s.recv(100)

print 'Received'

for i in data:
    print hex(ord(i))

s.close()
print "Socket closed"

