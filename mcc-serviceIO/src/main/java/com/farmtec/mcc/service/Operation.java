package com.farmtec.mcc.service;

public enum Operation {
    ReportUpdateRequest((byte)0x00),
    ReportUpdate((byte)0x01),
    UpdateMcu((byte)0x02);

    byte value;

    Operation(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
