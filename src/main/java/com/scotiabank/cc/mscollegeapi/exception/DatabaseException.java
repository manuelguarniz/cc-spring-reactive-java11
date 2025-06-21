package com.scotiabank.cc.mscollegeapi.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "cause", "stackTrace", "suppressed", "localizedMessage" })
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
