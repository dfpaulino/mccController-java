package com.farmtec.mcc.service;

public interface MccIpAddressResolver {
    String getIpForMccAdr(String MccAddr);

    //for future...
    String getIpForMccAdr(int mccAddr);
}
