package com.scotiabank.cc.mscollegeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DatabaseException.class)
    public Mono<ResponseEntity<DatabaseException>> handleBusinessException(DatabaseException e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new DatabaseException(e.getMessage())));
    }
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<BusinessException>> handleBusinessException(BusinessException e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BusinessException(e.getMessage())));
    }
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<BusinessException>> handleBusinessException(RuntimeException e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BusinessException(e.getMessage())));
    }
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<BusinessException>> handleBusinessException(Exception e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BusinessException(e.getMessage())));
    }
}
