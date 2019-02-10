package com.farmtec.mcc.service.impl;

import com.farmtec.mcc.dto.modules.PORTnDto;
import com.farmtec.mcc.models.modules.IO.PORTn;
import com.farmtec.mcc.repositories.PortRepository;
import com.farmtec.mcc.service.MccServiceErrorCode;
import com.farmtec.mcc.service.MccServiceException;
import com.farmtec.mcc.service.PortnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class PortnServiceImpl implements PortnService {

    @Autowired
    PortRepository portRepository;

    @Override
    public PORTn updateValue(int id, byte value) throws MccServiceException{
        PORTn porTn=getPortN(id);
        porTn.setValue(value);
        return porTn;
    }

    @Override
    public PORTn updateDDR(int id,byte ddr) throws MccServiceException{
        PORTn porTn=getPortN(id);
        porTn.setDdb(ddr);
        return porTn;
    }

    @Override
    public PORTn updateInUse(int id,boolean inUse) throws MccServiceException{
        PORTn porTn=getPortN(id);
        porTn.setInUse(inUse);
        return porTn;
    }

    @Override
    public PORTn getInfo(int id) throws MccServiceException{
        PORTn porTn=getPortN(id);
        return porTn;
    }


    private PORTn getPortN(int id) throws MccServiceException
    {
        try {
            PORTn porTn = portRepository.findById(id).get();
            return porTn;
        }catch (NoSuchElementException nse){
            throw new MccServiceException(nse,"Port not found", MccServiceErrorCode.DB_NOT_FOUND.getValue());
        }
    }
}
