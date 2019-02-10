package com.farmtec.mcc.dto.modules;



import com.farmtec.mcc.dto.BaseDto;
import com.farmtec.mcc.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter @Setter @NoArgsConstructor
public class PORTnDto extends BaseDto {

    int id;

    String portName;
    byte value;
    /*data direction
    0 - input
    1- output
     */
    byte ddb;

}
