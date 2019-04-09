package com.farmtec.mcc.controller;

import com.farmtec.mcc.controller.exception.ControllerExceptionHandler;
import com.farmtec.mcc.service.MccService;
import com.farmtec.mcc.dto.modules.AtmegaDto;
import com.farmtec.mcc.dto.utils.MccEntityToDto;
import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.factory.MccFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mcc")
public class MccController {

    @Autowired
    MccService mccService;


    @RequestMapping(value="/all",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<List<AtmegaDto>> getAllMcc()
    {
        System.out.println("Got to the controller getAllMcc()");
        List<Atmega> mccList=null;

            mccList=mccService.getAllMcu();
            if(mccList==null)
            {
                throw new ControllerExceptionHandler("No Mcc available",HttpStatus.NOT_FOUND);
            }
            else if(mccList.size()==0)
            {
                throw new ControllerExceptionHandler("No Mcc available",HttpStatus.NOT_FOUND);
            }

        List<AtmegaDto> dtoList =mccList.stream().map( atmega -> MccEntityToDto.mccEntityToDtoSimple(atmega)).collect(Collectors.toList());

        return  new ResponseEntity<>(dtoList,HttpStatus.OK);

    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AtmegaDto> getMccById(@PathVariable("id") byte id,@RequestParam(value="inUse",required = true) boolean inUse )
    {
        Atmega mcc=null;

            mcc=mccService.getMcuDetailsById(id);
            if(mcc==null)
            {
                throw new ControllerExceptionHandler("No Mcc available",HttpStatus.NOT_FOUND);
            }

        AtmegaDto atmegaDto=MccEntityToDto.mccEntityToDtoFull(mcc,inUse);
        return  new ResponseEntity<>(atmegaDto,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<AtmegaDto> addMcc(@RequestBody AtmegaDto mccDto)
    {
        Atmega mcc=mccService.createMcu(MccFactory.createMcc(mccDto.getAddress().replace("0x",""),mccDto.getModel(),mccDto.getName()));

        return  new ResponseEntity<>(MccEntityToDto.mccEntityToDtoSimple(mcc),HttpStatus.OK);
    }

    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteMccById(@PathVariable("id") byte id)
    {
        mccService.deleteMcc(id);
    }

}
