package br.com.pedromonteiro.order_service_api.controller.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import models.exceptions.GenericFeignException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    
    @ExceptionHandler(GenericFeignException.class)
    ResponseEntity<Map> handleGenericFeignException(final GenericFeignException e) {
        return ResponseEntity.status(e.getStatus())
                             .body(e.getError());
    }
}
