package com.farmtec.mcc.models.modules.timer;

import com.farmtec.mcc.models.Atmega;
import com.farmtec.mcc.models.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/*
 * we use default 8 bit timers
 * in the future we can include 16 bit
 * */
@Entity
@Table(name = "timer")
public class Timer extends BaseModel {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    //@GenericGenerator(name = "native", strategy = "native")
    //int id;

    String name;

    @Enumerated(EnumType.STRING)
    TimerMode mode;

    @Column(name = "out_put_compare_register")
    int outPutCompareRegister;

    @Basic
    boolean inUse;

    @ManyToOne
    @JoinColumn(name = "atmega_id")
    Atmega atmega;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimerMode getMode() {
        return mode;
    }

    public void setMode(TimerMode mode) {

        this.mode = mode;
    }
    public int getOutPutCompareRegister() {
        return this.outPutCompareRegister;
    }

    public void setOutPutCompareRegister(int outPutCompareRegister) {
        this.outPutCompareRegister = outPutCompareRegister;
    }

    public Atmega getAtmega() {
        return atmega;
    }

    public void setAtmega(Atmega atmega) {
        this.atmega = atmega;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    //TODO better ot move to a service class
    public int getPWMFreq(int prescaleFactor, int fclock) {
        int freq = 0;
        switch (this.mode) {
            case CTC:
                freq = fclock / (2 * prescaleFactor * (1 + this.outPutCompareRegister));
                break;
            case NORMAL:
                freq = 0;
                break;
            case FAST_PWM:
                freq = fclock / (256 * prescaleFactor);
                break;
            case PHASE_CORRECT_PWM:
                freq = fclock / (510 * prescaleFactor);
                break;

        }

        return freq;
    }

    public int getPWMpc() {
        int pwmPercentage = 0;
        switch (this.mode) {
            case CTC:
                pwmPercentage = 50;
                break;
            case NORMAL:
                pwmPercentage = 0;
                break;
            case FAST_PWM:
                pwmPercentage = Math.round(( (1 - ((float)(256 - this.outPutCompareRegister) / 256)) * 100));
                break;
            case PHASE_CORRECT_PWM:
                pwmPercentage = Math.round((1 - ((float)(510 - 2 * this.outPutCompareRegister) / 510)) * 100);
                break;

        }
        return pwmPercentage;
    }


}
