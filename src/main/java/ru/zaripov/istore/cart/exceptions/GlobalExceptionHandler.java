package ru.zaripov.istore.cart.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppException> handleResourceNotFoundException(ResourceNotFoundException e){
        log.warn(e.getMessage());
        return new ResponseEntity<>(new AppException("Resource not found", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
