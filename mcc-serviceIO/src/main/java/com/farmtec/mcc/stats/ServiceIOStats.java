package com.farmtec.mcc.stats;

import com.farmtec.mcc.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ManagedResource(objectName ="com.farmtec.mcc.stats:name=serviceIOStats",description = "ServiceIO Stats")
public class ServiceIOStats {

    Logger logger = LoggerFactory.getLogger(ServiceIOStats.class);

    private Map<Integer, ServiceIOMcuStats> mcuStatsMap = new ConcurrentHashMap<Integer, ServiceIOMcuStats>();

    public void updateMcuStats(ServiceIOMcuStats stats){
        ServiceIOMcuStats currentStats=mcuStatsMap.get(stats.getAddress());
        if(null!=currentStats) {
            //currentStats.addStats(stats);
            mcuStatsMap.computeIfPresent(stats.getAddress(), (k,v)->stats.addStats2(k,v));
        }
        else{
            currentStats=stats;
        }
        mcuStatsMap.put(stats.getAddress(),currentStats);
    }

    @Scheduled(fixedDelayString = "${stats.period.ms}")
    public void printStats(){
        //Date now=new Date();
        String statsString=getStatsFormat();
        System.out.println("Kicking in the stats schedule!!");
        logger.info("address,inboundMessage,outboundMessage,inBytes,outBytes\n"+statsString);

    }

    @ManagedOperation()
    public String getStatsFormat()
    {
        Date now=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb=new StringBuilder();
        for (Map.Entry address:this.mcuStatsMap.entrySet()) {
            Integer address1=(Integer) address.getKey();
            sb.append(format.format(now)).append(",")
             .append(Util.IntegerToByteReadableHex(address1)).append(",")
            .append(mcuStatsMap.get(address1).getInBoundMessages()).append(",")
            .append(mcuStatsMap.get(address1).getOutBoundMessages()).append(",")
            .append(mcuStatsMap.get(address1).getInBytes()).append(",")
            .append(mcuStatsMap.get(address1).getOutBytes()).append("\n");
        }
        return sb.toString();
    }
}
