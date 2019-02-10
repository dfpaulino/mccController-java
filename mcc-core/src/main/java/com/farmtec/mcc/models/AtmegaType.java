package com.farmtec.mcc.models;

public enum AtmegaType {
    ATMEGA18(0),
    ATMEGA32(1)
;
   private int model;

    AtmegaType(int model){this.model=model;};

    public int getValue(){return this.model;};

}
