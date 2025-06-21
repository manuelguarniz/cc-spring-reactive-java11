package com.scotiabank.cc.mscollegeapi.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "cause", "stackTrace", "suppressed", "localizedMessage" })
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
