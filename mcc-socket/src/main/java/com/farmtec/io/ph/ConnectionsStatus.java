package com.farmtec.io.ph;

public enum ConnectionsStatus {
    ACTIVE(1),NOT_ACTIVE(-1);

    int value;

    ConnectionsStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
