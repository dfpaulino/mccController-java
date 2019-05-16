package com.farmtec.mcc.controller;

import com.farmtec.mcc.cdr.dto.Cdr;
import com.farmtec.mcc.cdr.event.CdrEventPublisher;
import com.farmtec.mcc.controller.exception.ControllerExceptionHandler;
import com.farmtec.mcc.dto.modules.AtmegaDto;
import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.service.MccServiceException;
import com.farmtec.mcc.service.Operation;
import com.farmtec.mcc.service.ServiceIoSender;
import com.farmtec.mcc.service.TimerService;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.dto.utils.MccEntityToDto;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/timer")
@Setter
public class TimerController {

    @Autowired
    TimerService timerService;

    @Autowired
    ServiceIoSender serviceIoSender;

    @Autowired
    CdrEventPublisher cdrEventPublisher;

    @Value("${controller.update.mode:PROD}")
    String controller_mode;

    Logger logger = LoggerFactory.getLogger(TimerController.class);

    @RequestMapping(method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimerDto> updateTimer(@RequestBody TimerDto timerDto)
    {

        if(controller_mode.equals("PROD"))
        {
            AtmegaDto mccDto = new AtmegaDto();

            Timer timer=timerService.getInfo(timerDto.getId());

            mccDto.setAddress(timer.getAtmega().getAddress());
            TimerDto timerDto1=MccEntityToDto.timerEntityToTimerDto(timer);
            timerDto1.setOutPutCompareRegister(timerDto.getOutPutCompareRegister());
            List<TimerDto> timerDtos=new ArrayList<TimerDto>(1);
            timerDtos.add(timerDto1);
            mccDto.setTimers(timerDtos);
            if (!serviceIoSender.sendMessage(mccDto, Operation.UpdateMcu)) {
                logger.error("Error sending update message...");
                throw new ControllerExceptionHandler("Update MCU Failed",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //Generate Cdr Object
            Cdr cdr=new Cdr();
            cdr.setAddr(mccDto.getAddress());
            cdr.setOperation(Operation.UpdateMcu.getValue());
            cdr.setNow(new Date());
            cdr.setData("TIMER,"+timerDto1.getName()+","+timerDto1.getId()+","+timerDto1.getOutPutCompareRegister()+","+timerDto1.getMode().getValue());
            //publish event
            cdrEventPublisher.generateCdr(this,cdr);

            return new ResponseEntity<TimerDto>(MccEntityToDto.timerEntityToTimerDto(timer), HttpStatus.OK);
        }
        else {
            try {
                Timer timer = timerService.updateTimerById(timerDto);
                return new ResponseEntity<TimerDto>(MccEntityToDto.timerEntityToTimerDto(timer), HttpStatus.OK);
            } catch (MccServiceException mse) {
                throw new ControllerExceptionHandler(mse.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @RequestMapping(value = "/{id}/updateFlag",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTimerFlag(@PathVariable(value = "id") int id,
                                            @RequestParam(value = "inUse") boolean inUse)
    {
        timerService.setInUseFlag(id,inUse);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimerDto> getTimer(@PathVariable(value = "id") int id,
                                            @RequestParam(value = "inUse") boolean inUse)
    {
        Timer timer=timerService.getInfo(id);
        return new ResponseEntity<TimerDto>(MccEntityToDto.timerEntityToTimerDto(timer),HttpStatus.OK);
    }

}
