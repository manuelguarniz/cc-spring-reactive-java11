package com.scotiabank.cc.mscollegeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIErrorDTO {
  private LocalDateTime timestamp;
  private Integer status;
  private String error;
  private String message;
  private Map<String, String> errors;
}