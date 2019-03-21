package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.service.MccIpAddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MccIpAddressResolverImpl implements MccIpAddressResolver {

    Logger logger = LoggerFactory.getLogger(ServiceIoSenderImpl.class);

    Map<String,String> mccIpResolver=new HashMap<String,String>();

    @Value("${service.oi.mcc.ip.address.map}")
    void loadMap(String mccIp){
        String[] x=mccIp.split(";");
        int i=0;
        for (i=0;i<x.length;i+=1)
        {
            String[] x1=x[i].split(":");
            mccIpResolver.put(x1[0],x1[1]);
        }
        logger.debug("MccIp resolver loaded ["+mccIpResolver.toString()+"]");
    }

    @Override
    public String getIpForMccAdr(String mccAddr) {
        return mccIpResolver.get(mccAddr);
    }

    @Override
    public String getIpForMccAdr(int mccAddr) {
        return null;
    }
}
