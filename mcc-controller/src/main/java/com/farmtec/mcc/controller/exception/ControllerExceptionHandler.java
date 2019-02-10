package com.farmtec.mcc.controller.exception;

import org.springframework.http.HttpStatus;


public class ControllerExceptionHandler extends RuntimeException {

    ErrorResponse errorResponse;

    public ControllerExceptionHandler(String message, HttpStatus httpCode) {

        errorResponse = new ErrorResponse();
        errorResponse.message = message;
        errorResponse.httpCode = httpCode;
        errorResponse.exceptionMessage = this.getMessage();
    }

    public ErrorResponse getErrorResponse()
    {
        return this.errorResponse;
    }

}
