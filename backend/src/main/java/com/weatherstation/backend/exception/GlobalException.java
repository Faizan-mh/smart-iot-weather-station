package com.weatherstation.backend.exception;

import com.weatherstation.backend.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionResponse> duplicateResourceException(DuplicateResourceException e, HttpServletRequest request){
        ExceptionResponse response = new ExceptionResponse(
                400,"Duplicate", e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
