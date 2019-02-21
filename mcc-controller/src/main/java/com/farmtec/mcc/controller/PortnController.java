package com.farmtec.mcc.controller;

import com.farmtec.mcc.controller.exception.ControllerExceptionHandler;
import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.dto.utils.MccEntityToDto;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.service.MccServiceException;
import com.farmtec.mcc.service.PortnService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/port")
@Setter
public class PortnController {

    @Autowired
    private PortnService portnService;


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
            PORTn porTn = portnService.updateValue(id, value);
            return new ResponseEntity<PORTnDto>(MccEntityToDto.portEntityToPortDto(porTn), HttpStatus.OK);
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
