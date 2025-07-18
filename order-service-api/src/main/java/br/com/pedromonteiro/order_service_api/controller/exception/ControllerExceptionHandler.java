package br.com.pedromonteiro.order_service_api.controller.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import models.exceptions.GenericFeignException;
import models.exceptions.ResourceNotFoundException;
import models.exceptions.StandardError;
import models.exceptions.ValidationException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    
    @ExceptionHandler(GenericFeignException.class)
    ResponseEntity<Map> handleGenericFeignException(final GenericFeignException e) {
        return ResponseEntity.status(e.getStatus())
                             .body(e.getError());
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<StandardError> handleNotFoundException(final ResourceNotFoundException ex, final HttpServletRequest request) {

        var err = StandardError.builder()
        .timestamp(LocalDateTime.now())
        .status(NOT_FOUND.value())
        .error(NOT_FOUND.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
        return ResponseEntity.status(NOT_FOUND).body(err);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<StandardError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex, final HttpServletRequest request) {

        var err = ValidationException.builder()
        .timestamp(LocalDateTime.now())
        .status(BAD_REQUEST.value())
        .error("Validation Exception")
        .message("Exception in validation attributes")
        .path(request.getRequestURI())
        .errors(new ArrayList<>())
        .build();

        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            err.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(BAD_REQUEST).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<StandardError> handleDataIntegrityViolationException(final DataIntegrityViolationException ex, final HttpServletRequest request) {

        var err = StandardError.builder()
        .timestamp(LocalDateTime.now())
        .status(CONFLICT.value())
        .error(CONFLICT.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
        return ResponseEntity.status(CONFLICT).body(err);
    }
}
