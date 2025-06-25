package com.scotiabank.cc.mscollegeapi.controller;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
@Validated
@Tag(name = "Students", description = "API para gestión de estudiantes")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "Listar estudiantes", description = "Obtiene la lista de todos los estudiantes")
    public Flux<StudentDTO> listStudents() {
        return studentService
                .listStudents();
    }

    @PostMapping
    @Operation(summary = "Registrar estudiantes", description = "Registra estudiantes con o sin id (UUID) personalizado")
    public Mono<ResponseEntity<Object>> createStudent(@Valid @RequestBody StudentDTO request) {
        return studentService
                .createStudent(request)
                .doOnSuccess(
                        response -> log.info("Studiante creado con éxito : id: {}", response.getId()))
                .map(savedStudent -> ResponseEntity.status(HttpStatus.CREATED).build());
    }
}
