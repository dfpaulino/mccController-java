package com.farmtec.mcc.dto.modules;



import com.farmtec.mcc.dto.BaseDto;
import com.farmtec.mcc.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter @Setter @NoArgsConstructor
public class PORTnDto extends BaseDto {


    public int id;

    public String portName;

    private byte value;
    /*data direction
    0 - input
    1- output
     */
    private byte ddb;

    @JsonGetter("value")
    public String value()
    {
        return "0x"+Integer.toHexString(value & 0xFF);
    }

    @JsonGetter("ddb")
    public String ddb()
    {
        return "0x"+Integer.toHexString(ddb & 0xFF);
    }
}
