package com.farmtec.mcc.service;

public class MccServiceException extends RuntimeException{
    private String error;
    private int errorCode;


    public MccServiceException(String error, MccServiceErrorCode mccServiceErrorCode) {
        this.error = error;
        this.errorCode = mccServiceErrorCode.getValue();
    }

    public MccServiceException(Throwable cause, String error, int errorCode) {
        super(cause);
        this.error = error;
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }


    public int getErrorCode() {
        return errorCode;
    }


}
