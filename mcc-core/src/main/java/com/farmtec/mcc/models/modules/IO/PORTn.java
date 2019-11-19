package com.farmtec.mcc.models.modules.IO;


import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;

@Entity
@Table(name = "portn")
@Getter @Setter @NoArgsConstructor
public class PORTn extends BaseModel {
    String portName;
    byte value;
    /*data direction
    0 - input
    1- output
     */
    byte ddb;
    boolean inUse;

    @ManyToOne
    @JoinColumn(name = "atmega_id")
    Atmega atmega;

    public Atmega getAtmega() {
        return atmega;
    }

    public void setAtmega(Atmega atmega) {
        this.atmega = atmega;
    }
}
