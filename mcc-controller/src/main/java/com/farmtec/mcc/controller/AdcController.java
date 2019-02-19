package com.farmtec.mcc.controller;

import com.farmtec.mcc.dto.modules.AdcDto;
import com.farmtec.mcc.dto.utils.MccEntityToDto;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.service.AdcService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adc")
@Setter
public class AdcController {

    @Autowired
    AdcService adcService;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdcDto> getAdc(@PathVariable(name = "id") int id){
        ADC adc=adcService.getInfo(id);
        return new ResponseEntity<AdcDto>(MccEntityToDto.acdEntityToAdcDto(adc), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdcDto> updateAdc(@RequestBody AdcDto acdDto)
    {
        ADC adc=adcService.updateAcd(acdDto);
        return new ResponseEntity<AdcDto>(MccEntityToDto.acdEntityToAdcDto(adc), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/updateFlag",method = RequestMethod.PUT,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateAdc(@PathVariable(value = "id") int id,
                                            @RequestParam(value = "inUse") boolean inUse)
    {
        adcService.setInUseFlag(id,inUse);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
