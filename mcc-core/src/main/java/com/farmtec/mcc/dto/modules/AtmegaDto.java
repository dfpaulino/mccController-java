package com.farmtec.mcc.dto.modules;

import com.farmtec.mcc.dto.BaseDto;
import com.farmtec.mcc.models.AtmegaType;
import com.farmtec.mcc.models.BaseModel;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter @Getter @NoArgsConstructor
public class AtmegaDto extends BaseDto {

    private int id;

    private String name;

    @JsonIgnore
    private String address;

    private AtmegaType model;

    private List<TimerDto> timers;

    private List<PORTnDto> ioPort;

    private List<AdcDto> adcs;

    @JsonGetter("address")
    public String address() {
        if(0<=this.address.indexOf("0x"))
            return this.address;
        else
            return "0x"+this.address;
    }

    public AtmegaDto(AtmegaDto atmega)
    {
        this.name = atmega.getName();
        this.address = atmega.getAddress();
        this.model = atmega.getModel();

    }
}
