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
    @Deprecated
    public void addStats(ServiceIOMcuStats stats){
        this.inBoundMessages+=stats.inBoundMessages;
        this.outBoundMessages+=stats.outBoundMessages;
        this.inBytes+=stats.inBytes;
        this.outBytes+=stats.outBytes;
    }

    public ServiceIOMcuStats addStats2(Integer key,ServiceIOMcuStats stats){
        stats.inBytes+=this.inBytes;
        stats.inBoundMessages+=this.inBoundMessages;
        stats.outBytes+=this.outBytes;
        stats.outBoundMessages+=this.outBoundMessages;
        //System.out.println("Updating Stats for key "+key);
        return stats;
    }
}
