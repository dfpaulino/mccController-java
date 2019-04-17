package com.farmtec.mcc.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ServiceIOMcuStats {
    int address;
    int inBoundMessages;
    int outBoundMessages;
    int inBytes;
    int outBytes;
    public void addStats(ServiceIOMcuStats stats){
        this.inBoundMessages+=stats.inBoundMessages;
        this.outBoundMessages+=stats.outBoundMessages;
        this.inBytes+=stats.inBytes;
        this.outBytes+=stats.outBytes;
    }
}
