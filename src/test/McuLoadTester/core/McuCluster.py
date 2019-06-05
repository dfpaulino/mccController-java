#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Fri May 31 10:03:45 2019

@author: dpaulino
"""


from core.Mcu import Mcu

class McuCluster:
    
    def __init__(self,name):
        self.name=name
        self.cluster= []
        
    def setCluster(self,size):
        print "size is " + str(size)
        for i in range (size):
            strHex = "0x%0.2x" % i
            self.cluster.append(Mcu(str(i),strHex))
            print "added MCu to cluster"
            