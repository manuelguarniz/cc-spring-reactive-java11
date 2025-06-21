package com.scotiabank.cc.mscollegeapi.dto;

import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

  private String id;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(min = 1, max = 250, message = "El nombre debe tener entre 1 y 250 caracteres")
  @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
  private String name;

  @NotBlank(message = "El apellido es obligatorio")
  @Size(min = 1, max = 250, message = "El apellido debe tener entre 1 y 250 caracteres")
  @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
  private String lastName;

  @NotNull(message = "La edad es obligatoria")
  @Min(value = 1, message = "La edad debe ser mayor a 0")
  @Max(value = 70, message = "La edad debe ser menor o igual a 70")
  private Short age;

  @NotNull(message = "El estado es obligatorio")
  private StatusEnum status;

  public static StudentDTO fromEntity(Student student) {
    return StudentDTO.builder()
            .id(student.getId())
            .name(student.getName())
            .lastName(student.getLastName())
            .age(student.getAge())
            .status(StatusEnum.fromValue(student.getStatus()))
            .build();
  }
}