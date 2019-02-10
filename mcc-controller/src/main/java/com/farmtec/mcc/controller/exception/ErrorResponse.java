package com.farmtec.mcc.controller.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter @Getter
public class ErrorResponse {
    String message;
    HttpStatus httpCode;
    String exceptionMessage;

}
