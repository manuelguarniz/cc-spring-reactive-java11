package com.scotiabank.cc.mscollegeapi.controller;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.exception.BusinessException;
import com.scotiabank.cc.mscollegeapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public Flux<StudentDTO> listStudents() {
        return studentService
                .listStudents()
                .switchIfEmpty(Flux.error(new BusinessException("No students found")));
    }

    @PostMapping
    public Mono<ResponseEntity<?>> createStudent(@RequestBody StudentDTO studentDTO) {
        return studentService
                .createStudent(studentDTO)
                .map(savedStudent -> ResponseEntity.status(HttpStatus.CREATED).build());
    }
}
