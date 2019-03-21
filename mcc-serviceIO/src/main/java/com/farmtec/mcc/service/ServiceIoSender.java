package com.farmtec.mcc.service;

import com.farmtec.mcc.dto.modules.AtmegaDto;

public interface ServiceIoSender {
    public boolean sendMessage(AtmegaDto mccDto,Operation operation);
}
