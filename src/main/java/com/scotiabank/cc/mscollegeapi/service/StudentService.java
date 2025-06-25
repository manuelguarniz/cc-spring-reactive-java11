package com.scotiabank.cc.mscollegeapi.service;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {
    Flux<StudentDTO> listStudents();
    Mono<StudentDTO> createStudent(StudentDTO request);
    Mono<StudentDTO> getStudentById(String id);
}
