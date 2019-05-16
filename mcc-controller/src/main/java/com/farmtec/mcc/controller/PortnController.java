package com.farmtec.mcc.controller;

import com.farmtec.mcc.cdr.dto.Cdr;
import com.farmtec.mcc.cdr.event.CdrEvent;
import com.farmtec.mcc.cdr.event.CdrEventPublisher;
import com.farmtec.mcc.controller.exception.ControllerExceptionHandler;
import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.modules.AtmegaDto;
import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.dto.utils.MccEntityToDto;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.service.*;
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
@RequestMapping("/port")
@Setter
public class PortnController {

    @Value("${controller.update.mode:PROD}")
    String controller_mode;

    @Autowired
    private PortnService portnService;

    @Autowired
    ServiceIoSender serviceIoSender;

    @Autowired
    CdrEventPublisher cdrEventPublisher;

    Logger logger = LoggerFactory.getLogger(PortnController.class);


    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PORTnDto> getPortInfo(@PathVariable(name = "id") int id){
        PORTn porTn=portnService.getInfo(id);
        return new ResponseEntity<PORTnDto>(MccEntityToDto.portEntityToPortDto(porTn), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/value",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PORTnDto> updatePortValue(@PathVariable(name = "id") int id, @RequestParam(name = "value") String strValue)
    {
        byte value=(byte) (0xFF&Integer.valueOf(strValue,16));
        try {
            if(controller_mode.equals("PROD")) {
                AtmegaDto mccDto = new AtmegaDto();
                mccDto.setAddress(portnService.getInfo(id).getAtmega().getAddress());
                List<PORTnDto> porTnDtos = new ArrayList<PORTnDto>(1);
                PORTnDto porTnDto = MccEntityToDto.portEntityToPortDto(portnService.getInfo(id));
                porTnDto.setValue(value);
                porTnDtos.add(porTnDto);
                mccDto.setIoPort(porTnDtos);
                if (!serviceIoSender.sendMessage(mccDto, Operation.UpdateMcu)) {
                    logger.error("Error sending update message...");
                    throw new ControllerExceptionHandler("Update MCU Failed",HttpStatus.INTERNAL_SERVER_ERROR);
                }
                //Generate Cdr Object
                Cdr cdr=new Cdr();
                cdr.setAddr(mccDto.getAddress());
                cdr.setOperation(Operation.UpdateMcu.getValue());
                cdr.setNow(new Date());
                cdr.setData("PORT,"+porTnDto.getPortName()+","+porTnDto.getId()+","+porTnDto.getValue()+","+porTnDto.getDdb());
                //publish event
                cdrEventPublisher.generateCdr(this,cdr);

                return new ResponseEntity<PORTnDto>(porTnDto, HttpStatus.OK);

            }else {
                PORTn porTn = portnService.updateValue(id, value);
                return new ResponseEntity<PORTnDto>(MccEntityToDto.portEntityToPortDto(porTn), HttpStatus.OK);
            }
        }catch (MccServiceException mse)
        {
             throw new ControllerExceptionHandler("No Port available",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}/ddr",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PORTnDto> updatePortDDR(@PathVariable(name = "id") int id, @RequestParam(name = "value") String strValue)
    {
        byte value=(byte) (0xFF&Integer.valueOf(strValue,16));

        try{
            PORTn porTn=portnService.updateDDR(id,value);
            return new ResponseEntity<PORTnDto>(MccEntityToDto.portEntityToPortDto(porTn), HttpStatus.OK);
        }catch (MccServiceException mse)
        {
            throw new ControllerExceptionHandler("No Port available",HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/{id}/updateFlag",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatePortFlag(@PathVariable(value = "id") int id,
                                            @RequestParam(value = "inUse") boolean inUse)
    {
        portnService.updateInUse(id,inUse);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
