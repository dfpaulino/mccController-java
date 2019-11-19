package com.farmtec.mcc.models.modules.adc;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "adc")
@Getter @Setter @NoArgsConstructor
public class ADC extends BaseModel {

    byte adcId;
    int value;
    boolean inUse;

    @ManyToOne
    @JoinColumn(name = "atmega_id")
    Atmega atmega;

}
