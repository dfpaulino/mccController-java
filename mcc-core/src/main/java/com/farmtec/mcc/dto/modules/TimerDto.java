package com.farmtec.mcc.dto.modules;

/*
 * we use default 8 bit timers
 * in the future we can include 16 bit
 * */

import com.farmtec.mcc.dto.BaseDto;
import com.farmtec.mcc.models.modules.timer.TimerMode;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
public class TimerDto extends BaseDto {

    private int id;
    private String name;

    private TimerMode mode;

    private int outPutCompareRegister;


    private int pwmPc;
    @JsonIgnore
    private int pwmFreq;


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



    //TODO better ot move to a service class
    @JsonGetter("pwmFreq")
    public int getPwmFreq(int prescaleFactor, int fclock) {
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

    @JsonGetter("pwmPc")
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
