package com.scotiabank.cc.mscollegeapi.service;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.enums.StatusEnum;
import com.scotiabank.cc.mscollegeapi.exception.DatabaseException;
import com.scotiabank.cc.mscollegeapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO createRequest;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId("1");
        student.setName("Juan");
        student.setLastName("Perez");
        student.setAge((short) 20);
        student.setStatus(StatusEnum.ACTIVE.getValue());

        createRequest = StudentDTO.builder()
                .name("Juan")
                .lastName("Perez")
                .age((short) 20)
                .status(StatusEnum.ACTIVE)
                .build();
    }

    @Test
    void listStudents_returnsStudents() {
        when(studentRepository.findAll()).thenReturn(Flux.just(student));
        StepVerifier.create(studentService.listStudents())
                .expectNext(StudentDTO.fromEntity(student))
                .verifyComplete();
    }

    @Test
    void listStudents_returnsEmpty() {
        when(studentRepository.findAll()).thenReturn(Flux.empty());
        StepVerifier.create(studentService.listStudents())
                .verifyComplete();
    }

    @Test
    void createStudent_success() {
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(student));
        StepVerifier.create(studentService.createStudent(createRequest))
                .expectNext(StudentDTO.fromEntity(student))
                .verifyComplete();
    }

    @Test
    void createStudent_error() {
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.error(new RuntimeException("DB error")));
        StepVerifier.create(studentService.createStudent(createRequest))
                .expectError(DatabaseException.class)
                .verify();
    }
}