package com.example.util.exception_handler;


import com.example.dto.exception.ErrorResponseDTO;
import com.example.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        log.error("Validation error: {}", errors);
        return new ResponseEntity<>(new ErrorResponseDTO(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ClientException.class)
    public final ResponseEntity<ErrorResponseDTO> handleClientExceptions(ClientException ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public final ResponseEntity<ErrorResponseDTO> handleNoResourceFoundExceptions(NoResourceFoundException ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(errors), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponseDTO> handleGeneralExceptions(Exception ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(errors), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }




}
