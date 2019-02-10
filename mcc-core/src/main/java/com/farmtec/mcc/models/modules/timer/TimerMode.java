package com.farmtec.mcc.models.modules.timer;

public enum TimerMode {
    NORMAL(1),
    PHASE_CORRECT_PWM(2),
    CTC(3),
    FAST_PWM(4)


;
    private int mode;


    TimerMode(int mode) {
        this.mode = mode;
    }

    public int getValue() {
        return mode;
    }
}
