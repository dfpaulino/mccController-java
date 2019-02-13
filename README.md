# mccController-java
MicroController Monitor 
this is to monotor and contol a cluster of microcontroler (ATMEL).
A cluster is formed by N Mcc.
Each Mcc should be created via REST API (where you specify the type of ATMEL..see supporte version)

Note Multiple cluster are not supported yet. Currently its only possible to configure 1 cluster witn N ATMEGA's. This will be in the future.

A client can then consult ALL th Mcc's available, and the values of their modules (IO port, ADC,TIMER)
An idividual module can be consuled/updated provided by its own ID.
The Module ID is extracted from the Parent -> MCC getDetailInfoByMccId()


This will have a controller for
* MCC (get all MCC available, getDetailInfoByMccId(ID))
* Timers (getInfo(ID); updateCounter(ID),enable/disable)
* ADC
* PORTs

## ###########################################
##       REST END POINTS
## ##########################################

## MCC
	

## TIMER

## ADC


## IO PORT




# ################################
#
#      Supported Versions
# ###############################
ATMEGA32
ATMEGA16

