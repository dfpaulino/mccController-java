#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Wed Jun  5 13:53:48 2019

@author: dpaulino
"""

import requests

from threading import Lock,Thread
import datetime


class HttpRequest():
    
    lock = Lock()
    count = 0
    
    def __init__(self,uri,method):
        #threading.Thread.__init__(self)
        self.uri=uri
        self.method=method

    def incrementCount():
        self.lock.acquire()
        self.count+=1
        self.lock.release()
        
        
    def doPost(self,body):
        headers = {'Content-Type': 'application/json'}
        reqStart = datetime.datetime.now()
        response = requests.post(self.uri, json=body,headers=headers)
        reqStop = datetime.datetime.now()
        tdelta=reqStop-reqStart
        print ('Response Time: ' + (str(tdelta.total_seconds() * 1000)) + ' ms')
        if response.status_code == requests.codes.ok:
            print ('Response code '+ str(requests.codes.ok))
            print ('Created ID=' + str(response.json()['id']))
        else:
            print (response.status_code)
        return response
            
    def doGet(self):
        headers = {'Accept': 'application/json'}
        reqStart = datetime.datetime.now()
        response = requests.get(self.uri,headers=headers)
        reqStop = datetime.datetime.now()
        tdelta=reqStop-reqStart
        
        if response.status_code == requests.codes.ok:
            print ('Response code 200 in ' + (str(tdelta.total_seconds() * 1000)) + ' ms')
            print ('Status: ' + response.json()["status"])
            return
        else:
            print ('Error Response code '+response.status_code)
            
    def execute(self):
        if self.method == 'GET':
            self.doGet()
        elif self.method == 'POST':
            self.doPost(self.body)
            
            
        
            
        
        