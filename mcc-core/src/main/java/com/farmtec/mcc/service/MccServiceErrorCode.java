package com.farmtec.mcc.service;

public enum MccServiceErrorCode {
    DB_ERROR(1),
    DB_NOT_FOUND(2);

    int errorCode;

    MccServiceErrorCode(int error)
    {
        this.errorCode=error;
    }
    public int getValue(){return this.errorCode;}

}
