package com.scotiabank.cc.mscollegeapi.validation;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.enums.StatusEnum;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentValidationTest {

  private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private final Validator validator = factory.getValidator();

  @Test
  void validStudent_shouldPassValidation() {
    StudentDTO request = StudentDTO.builder()
        .name("Juan")
        .lastName("Perez")
        .age((short) 25)
        .status(StatusEnum.ACTIVE)
        .build();

    Set<ConstraintViolation<StudentDTO>> violations = validator.validate(request);
    assertTrue(violations.isEmpty(), "Should have no validation errors");
  }

  @Test
  void invalidName_shouldFailValidation() {
    StudentDTO request = StudentDTO.builder()
        .name("")
        .lastName("Perez")
        .age((short) 25)
        .status(StatusEnum.ACTIVE)
        .build();

    Set<ConstraintViolation<StudentDTO>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Should have validation errors");
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
  }

  @Test
  void invalidAge_shouldFailValidation() {
    StudentDTO request = StudentDTO.builder()
        .name("Juan")
        .lastName("Perez")
        .age((short) 0)
        .status(StatusEnum.ACTIVE)
        .build();

    Set<ConstraintViolation<StudentDTO>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Should have validation errors");
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("age")));
  }

  @Test
  void nullStatus_shouldFailValidation() {
    StudentDTO request = StudentDTO.builder()
        .name("Juan")
        .lastName("Perez")
        .age((short) 25)
        .status(null)
        .build();

    Set<ConstraintViolation<StudentDTO>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Should have validation errors");
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
  }

  @Test
  void nameTooLong_shouldFailValidation() {
    String longName = "A".repeat(251);
    StudentDTO request = StudentDTO.builder()
        .name(longName)
        .lastName("Perez")
        .age((short) 25)
        .status(StatusEnum.ACTIVE)
        .build();

    Set<ConstraintViolation<StudentDTO>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Should have validation errors");
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
  }

  @Test
  void ageTooHigh_shouldFailValidation() {
    StudentDTO request = StudentDTO.builder()
        .name("Juan")
        .lastName("Perez")
        .age((short) 71)
        .status(StatusEnum.ACTIVE)
        .build();

    Set<ConstraintViolation<StudentDTO>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Should have validation errors");
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("age")));
  }
}