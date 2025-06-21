package com.scotiabank.cc.mscollegeapi.dto;

import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDTO {
    private String id;
    private String name;
    private String lastName;
    private Short age;
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
