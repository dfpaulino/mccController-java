package com.farmtec.mcc.service;

import com.farmtec.mcc.models.modules.IO.PORTn;

public interface PortnService {

    public PORTn updateValue(int id, byte value) throws MccServiceException;

    public PORTn updateDDR(int id ,byte ddr) throws MccServiceException;

    public PORTn updateInUse(int id,boolean inUse) throws MccServiceException;

    public PORTn getInfo(int id) throws MccServiceException;

}
