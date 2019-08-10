#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Tue Jul 30 21:35:20 2019

@author: daniel
"""

from rest.HttpRequest import HttpRequest
from concurrent.futures import ThreadPoolExecutor
from rest.HttpRequest import HttpRequest
from core.Mcu import Mcu
import json
class RestCreateMcu:
    
    def __init__(self,mcuCluster):
        self.cluster=mcuCluster
        #self.executor=null
        print "setting cluster "+self.cluster.name
        
    def setThreadPool(self,MaxThr=3):
        self.executor=ThreadPoolExecutor(MaxThr)
        
    def createMcus(self):
        httpRequest=HttpRequest("http://localhost:8080/mcc","POST")
        
        for mcu in self.cluster.cluster:
            address=mcu.address
            name=mcu.name
            body={"name":name,"address":address,"model":"ATMEGA32"}
            #body={"name":""name"","address":,"model":"ATMEGA32"}
            #print ">>>>>"+json.dump(body)
            self.executor.submit(httpRequest.doPost,body)
            
        
    