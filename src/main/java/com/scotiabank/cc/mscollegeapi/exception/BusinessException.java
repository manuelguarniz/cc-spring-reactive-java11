package com.scotiabank.cc.mscollegeapi.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonIgnoreProperties({ "cause", "stackTrace", "suppressed", "localizedMessage" })
public class BusinessException extends RuntimeException {
    HttpStatus httpStatus;
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
