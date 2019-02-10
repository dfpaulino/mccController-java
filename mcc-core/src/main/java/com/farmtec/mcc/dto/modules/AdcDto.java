package com.farmtec.mcc.dto.modules;


import com.farmtec.mcc.dto.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter @Setter @NoArgsConstructor
public class AdcDto extends BaseDto {

    int id;

    byte adcId;
    int value;

}
