package com.farmtec.mcc.controller;

import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.dto.utils.MccEntityToDto;
import com.farmtec.mcc.models.modules.IO.PORTn;
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
    PortnService portnService;


    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PORTnDto> getAdc(@PathVariable(name = "id") int id){
        PORTn porTn=portnService.getInfo(id);
        return new ResponseEntity<PORTnDto>(MccEntityToDto.portEntityToPortDto(porTn), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/value",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PORTnDto> updateAdcValue(@PathVariable(name = "id") int id, byte value)
    {

        PORTn porTn=portnService.updateValue(id,value);
        return new ResponseEntity<PORTnDto>(MccEntityToDto.portEntityToPortDto(porTn), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/ddr",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PORTnDto> updateAdcDDR(@PathVariable(name = "id") int id, byte value)
    {
        PORTn porTn=portnService.updateDDR(id,value);
        return new ResponseEntity<PORTnDto>(MccEntityToDto.portEntityToPortDto(porTn), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/updateFlag",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTimer(@PathVariable(value = "id") int id,
                                            @RequestParam(value = "inUse") boolean inUse)
    {
        portnService.updateInUse(id,inUse);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
