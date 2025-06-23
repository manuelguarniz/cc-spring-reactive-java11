package com.scotiabank.cc.mscollegeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.enums.StatusEnum;
import com.scotiabank.cc.mscollegeapi.exception.BusinessException;
import com.scotiabank.cc.mscollegeapi.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StudentService studentService;

    @Test
    void listStudents_shouldReturnStudents_whenStudentsExist() {
        List<StudentDTO> students = Arrays.asList(
                createStudentDTO("Juan", "Perez", (short) 25),
                createStudentDTO("Maria", "Garcia", (short) 30),
                createStudentDTO("Carlos", "Lopez", (short) 28));

        when(studentService.listStudents()).thenReturn(Flux.fromIterable(students));

        webTestClient.get()
                .uri("/api/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentDTO.class)
                .hasSize(3)
                .contains(students.toArray(new StudentDTO[0]));
    }

    @Test
    void listStudents_shouldReturnSingleStudent_whenOneStudentExists() {
        StudentDTO student = createStudentDTO("Ana", "Rodriguez", (short) 22);
        when(studentService.listStudents()).thenReturn(Flux.just(student));

        webTestClient.get()
                .uri("/api/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentDTO.class)
                .hasSize(1)
                .contains(student);
    }

    @Test
    void listStudents_shouldReturnCorrectContentType_whenStudentsExist() {
        StudentDTO student = createStudentDTO("Test", "User", (short) 25);
        when(studentService.listStudents()).thenReturn(Flux.just(student));

        webTestClient.get()
                .uri("/api/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void listStudents_shouldHandleServiceError() {
        when(studentService.listStudents()).thenReturn(
                Flux.error(new RuntimeException("Database connection failed")));

        webTestClient.get()
                .uri("/api/students")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void createStudent_withValidRequest_shouldCreateStudent() {
        StudentDTO student = createStudentDTO("Juan", "Perez", (short) 25);

        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(Mono.just(student));

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), StudentDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

    @Test
    void createStudent_withEmptyName_shouldReturnBadRequest() {
        StudentDTO student = createStudentDTO(null, "Perez", (short) 25);

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), StudentDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.errors.name").exists();
    }

    @Test
    void createStudent_withInvalidAge_shouldReturnBadRequest() {
        StudentDTO student = createStudentDTO("Juan", "Perez", (short) -17);

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), StudentDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.errors.age").exists();
    }

    @Test
    void createStudent_withInvalidStatus_shouldReturnBadRequest() {
        StudentDTO student = createStudentDTO("Juan", "Perez", (short) 17);

        student.setStatus(null);

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), StudentDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.errors.status").exists();
    }

    @Test
    void createStudent_withDuplicateId_shouldReturnBusinessException() {
        StudentDTO student = createStudentDTO("Juan", "Perez", (short) 17);

        when(studentService.createStudent(any(StudentDTO.class)))
                .thenReturn(Mono.error(new BusinessException("Duplicate Id Error", HttpStatus.BAD_REQUEST)));

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(student), StudentDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Business Validation Error")
                .jsonPath("$.message").exists()
                .jsonPath("$.message").isEqualTo("Duplicate Id Error");

    }

    private StudentDTO createStudentDTO(String name, String lastName, Short age) {
        return StudentDTO.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .lastName(lastName)
                .age(age)
                .status(StatusEnum.ACTIVE)
                .build();
    }
}