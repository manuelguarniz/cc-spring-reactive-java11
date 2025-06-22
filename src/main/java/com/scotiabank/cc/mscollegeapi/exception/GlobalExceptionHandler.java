package com.scotiabank.cc.mscollegeapi.exception;

import com.scotiabank.cc.mscollegeapi.dto.APIErrorDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<APIErrorDTO> handleValidationExceptions(WebExchangeBindException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });

    APIErrorDTO errorResponse = APIErrorDTO.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Validation Error")
        .message("Los datos proporcionados no son válidos")
        .errors(errors)
        .build();

    log.error("Validation error: {}", errors);
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<APIErrorDTO> handleBusinessException(BusinessException ex) {
    APIErrorDTO errorResponse = APIErrorDTO.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.CONFLICT.value())
        .error("Business Validation Error")
        .message(ex.getMessage())
        .build();

    log.warn("Business rule violation: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<APIErrorDTO> handleDatabaseException(DatabaseException ex) {
    APIErrorDTO errorResponse = APIErrorDTO.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error("Database Error")
        .message(ex.getMessage())
        .build();

    log.error("Database error: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIErrorDTO> handleGenericException(Exception ex) {
    APIErrorDTO errorResponse = APIErrorDTO.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error("Internal Server Error")
        .message("Ha ocurrido un error interno del servidor")
        .build();

    log.error("Unexpected error: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}