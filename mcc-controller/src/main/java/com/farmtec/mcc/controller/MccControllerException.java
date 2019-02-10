package com.farmtec.mcc.controller;

import com.farmtec.mcc.controller.exception.ControllerExceptionHandler;
import com.farmtec.mcc.controller.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class MccControllerException {

    @ExceptionHandler(ControllerExceptionHandler.class)
    ResponseEntity<ErrorResponse> exceptionHandler(ControllerExceptionHandler e)
    {
        return new ResponseEntity<ErrorResponse>(e.getErrorResponse(),e.getErrorResponse().getHttpCode());
    }
}
