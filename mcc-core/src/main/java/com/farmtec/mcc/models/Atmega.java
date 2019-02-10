package com.farmtec.mcc.models;

import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.models.modules.adc.ADC;
import com.farmtec.mcc.models.modules.timer.Timer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "atmega")
@Setter @Getter @NoArgsConstructor
public class Atmega extends  BaseModel {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String address;

    @Enumerated(EnumType.ORDINAL)
    AtmegaType model;

    @OneToMany(mappedBy = "atmega",cascade = CascadeType.ALL)
    List<Timer> timers;

    @OneToMany(mappedBy = "atmega",cascade = CascadeType.ALL)
    List<PORTn> ioPort;

    @OneToMany(mappedBy = "atmega",cascade = CascadeType.ALL)
    List<ADC> adcs;

    public Atmega(String name, String address, AtmegaType model, List<Timer> timers, List<PORTn> ioPort, List<ADC> adcs) {
        this.name = name;
        this.address = address;
        this.model = model;
        this.timers = timers;
        this.ioPort = ioPort;
        this.adcs = adcs;
    }

    public Atmega(Atmega atmega)
    {
        this.name = atmega.getName();
        this.address = atmega.getAddress();
        this.model = atmega.getModel();

    }

    public boolean updateTimers(List<Timer> timers)
    {
        if (this.timers.size() != timers.size()) {
            System.out.println("Timer size do not match");
            return false;
        }
        for (int idx = 0; idx < this.getTimers().size(); idx++) {
            this.getTimers().get(idx).setOutPutCompareRegister(timers.get(idx).getOutPutCompareRegister());
        }
        return true;
    }

    //TODO this is updating the whole lot. Perhaps its better to compare the value before updating...
    public boolean updateValues(Atmega atmega)
    {
        if(this.address.equals(atmega.getAddress())) {

            if(!updateTimers(atmega.getTimers()))
                return false;
            /*
            if (this.timers.size() != atmega.timers.size()) {
                System.out.println("Timer size do not match");
                return false;
            }
            for (int idx = 0; idx < this.getTimers().size(); idx++) {
                this.getTimers().get(idx).setOutPutCompareRegister(atmega.getTimers().get(idx).getOutPutCompareRegister());
            }
            */

            if (this.adcs.size() != atmega.adcs.size()) {
                System.out.println("Adc size do not match");
                return false;
            }
            for (int idx = 0; idx < this.getAdcs().size(); idx++) {
                this.getAdcs().get(idx).setValue(atmega.getAdcs().get(idx).getValue());
            }


            if (this.ioPort.size() != atmega.ioPort.size()) {
                System.out.println("IO PORT size do not match");
                return false;
            }
            for (int idx = 0; idx < this.getIoPort().size(); idx++) {
                this.getIoPort().get(idx).setValue(atmega.getIoPort().get(idx).getValue());
            }
        }
        else
        {
            return false;
        }
        return true;
    }
}
