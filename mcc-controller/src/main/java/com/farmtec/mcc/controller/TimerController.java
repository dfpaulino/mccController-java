package com.farmtec.mcc.controller;

import com.farmtec.mcc.models.modules.timer.Timer;
import com.farmtec.mcc.service.TimerService;
import com.farmtec.mcc.dto.modules.TimerDto;
import com.farmtec.mcc.dto.utils.MccEntityToDto;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timer")
@Setter
public class TimerController {

    @Autowired
    TimerService timerService;



    @RequestMapping(method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimerDto> updateTimer(@RequestBody TimerDto timerDto)
    {
        Timer timer=timerService.updateTimerById(timerDto);
        return new ResponseEntity<TimerDto>(MccEntityToDto.timerEntityToTimerDto(timer),HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/updateFlag",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTimer(@PathVariable(value = "id") int id,
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
