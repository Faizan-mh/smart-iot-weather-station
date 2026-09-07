package com.weatherstation.backend.dto;

import java.util.Map;

public class ValidationExceptionResponse extends ExceptionResponse{
    Map<String,String> errors;

    public ValidationExceptionResponse(int statuscode, String error, String message, String path,Map<String,String> errors){
        super(statuscode,error,message,path);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
