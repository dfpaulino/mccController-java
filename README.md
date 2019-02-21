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
1. Add MUC
    1. POST /mcu
    
    body: MCU (see Types)
    
    http://localhost:8080/mcc
    
    
    {
        "created": "21-02-2019 09:24:23",
        "updated": "21-02-2019 09:24:23",
        "inUse": true,
        "id": 1,
        "name": "nameX13",
        "address": "F13",
        "model": "ATMEGA18"
    }
    
    
    model is a ENUM with the supported MCU

1. GetMcuDetails
    1. /mcu/{id}inUse=true|false
    
    http://localhost:8080/mcc/1?inUse=true
    
    
    {
        "created": "21-02-2019 09:24:23",
        "updated": "21-02-2019 09:24:23",
        "inUse": true,
        "id": 1,
        "name": "nameX13",
        "address": "F13",
        "model": "ATMEGA18",
        "timers": [
            {
                "created": "21-02-2019 09:24:23",
                "updated": "21-02-2019 09:24:23",
                "inUse": true,
                "id": 1,
                "name": "timer0",
                "mode": "FAST_PWM",
                "outPutCompareRegister": 128,
                "pwmPc": 50
            },
            {
                "created": "21-02-2019 09:24:23",
                "updated": "21-02-2019 09:24:23",
                "inUse": true,
                "id": 2,
                "name": "timer1",
                "mode": "FAST_PWM",
                "outPutCompareRegister": 128,
                "pwmPc": 50
            },
            {
                "created": "21-02-2019 09:24:23",
                "updated": "21-02-2019 09:24:23",
                "inUse": true,
                "id": 3,
                "name": "timer3",
                "mode": "FAST_PWM",
                "outPutCompareRegister": 128,
                "pwmPc": 50
            }
        ],
        "ioPort": [
            {
                "inUse": true,
                "id": 1,
                "portName": "PORTA",
                "value": "0x0",
                "ddb": "0x0"
            },
            {
                "inUse": true,
                "id": 2,
                "portName": "PORTB",
                "value": "0x0",
                "ddb": "0x0"
            },
            {
                "inUse": true,
                "id": 3,
                "portName": "PORTC",
                "value": "0x0",
                "ddb": "0x0"
            },
            {
                "inUse": true,
                "id": 4,
                "portName": "PORTD",
                "value": "0x0",
                "ddb": "0x0"
            }
        ],
        "adcs": [
            {
                "inUse": true,
                "id": 1,
                "adcId": 0,
                "value": 0
            },
            {
                "inUse": true,
                "id": 2,
                "adcId": 1,
                "value": 0
            },
            {
                "inUse": true,
                "id": 3,
                "adcId": 2,
                "value": 0
            },
            {
                "inUse": true,
                "id": 4,
                "adcId": 3,
                "value": 0
            },
            {
                "inUse": true,
                "id": 5,
                "adcId": 4,
                "value": 0
            },
            {
                "inUse": true,
                "id": 6,
                "adcId": 5,
                "value": 0
            },
            {
                "inUse": true,
                "id": 7,
                "adcId": 6,
                "value": 0
            },
            {
                "inUse": true,
                "id": 8,
                "adcId": 7,
                "value": 0
            }
        ]
    }
    
1. GetAllMcu
   1. /mcu/all?inUse=true|false
  
   http://localhost:8080/mcc/all
  
   
       [
           {
               "created": "21-02-2019 09:24:23",
               "updated": "21-02-2019 09:24:23",
               "inUse": true,
               "id": 1,
               "name": "nameX13",
               "address": "F13",
               "model": "ATMEGA18"
           },
           {
               "created": "21-02-2019 09:28:31",
               "updated": "21-02-2019 09:28:31",
               "inUse": true,
               "id": 2,
               "name": "nameX13c",
               "address": "F12",
               "model": "ATMEGA18"
           }
       ]
   


## TIMER

1. get Details by Id

      http://localhost:8080/timer/1?inUse=true

        
        {
            "created": "21-02-2019 09:24:23",
            "updated": "21-02-2019 09:24:23",
            "inUse": true,
            "id": 1,
            "name": "timer0",
            "mode": "FAST_PWM",
            "outPutCompareRegister": 128,
            "pwmPc": 50
        }

1. update value

This should update the OCN of the timer on the MCU

        PUT
        http://localhost:8080/timer/
         {
           "id": 1,
           "mode": "FAST_PWM",
           "outPutCompareRegister": 235
         }
         
      respose
      
          {
              "created": "21-02-2019 09:24:23",
              "updated": "21-02-2019 09:36:14",
              "inUse": true,
              "id": 1,
              "name": "timer0",
              "mode": "FAST_PWM",
              "outPutCompareRegister": 235,
              "pwmPc": 92
          }

## ADC


## IO PORT

1. get port info

GET

http://localhost:8080/port/1

    {
        "inUse": true,
        "id": 1,
        "portName": "PORTA",
        "value": "0x0",
        "ddb": "0x0"
    }
    
1. update port value

This should update the OutPut pins of the MCU

PUT

http://localhost:8080/port/1/value?value=ff

    {
        "inUse": true,
        "id": 1,
        "portName": "PORTA",
        "value": "0xff",
        "ddb": "0x0"
    }

1. update port Data Direction Register
This does not change the DDR on the MCU, only updates the database

PUT

http://localhost:8080/port/1/ddr?value=ff

    {
        "inUse": true,
        "id": 1,
        "portName": "PORTA",
        "value": "0xff",
        "ddb": "0xff"
    }


# ################################
#
#      Supported Versions MCU
# ###############################
1 ATMEGA32

1.ATMEGA16



#################################
#
#  Types
#################################
###MCU
    "{
    	"name":"nameX13",
    	"address":"0xF13",
    	"model":"ATMEGA32"
    }"
    
