package br.com.pedromonteiro.user_service_api.controller.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import models.exceptions.ResourceNotFoundException;
import models.exceptions.StandardError;


@ControllerAdvice
public class ControllerExceptionHandler {
 
    
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<?> handleNotFoundException(final ResourceNotFoundException ex, final HttpServletRequest request) {

        var err = StandardError.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }
}
