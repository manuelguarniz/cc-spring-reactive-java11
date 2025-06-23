package com.scotiabank.cc.mscollegeapi.service;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.enums.StatusEnum;
import com.scotiabank.cc.mscollegeapi.exception.BusinessException;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO createRequest;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        student = new Student();
        student.setId(testId);
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
    void createStudent_withoutId_success() {
        when(studentRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(student));

        StepVerifier.create(studentService.createStudent(createRequest))
                .expectNext(StudentDTO.fromEntity(student))
                .verifyComplete();
    }

    @Test
    void createStudent_withId_success() {
        createRequest.setId(testId.toString());
        when(studentRepository.findById(testId)).thenReturn(Mono.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(student));

        StepVerifier.create(studentService.createStudent(createRequest))
                .expectNext(StudentDTO.fromEntity(student))
                .verifyComplete();
    }

    @Test
    void createStudent_withExistingId_throwsBusinessException() {
        createRequest.setId(testId.toString());
        when(studentRepository.findById(testId)).thenReturn(Mono.just(student));

        StepVerifier.create(studentService.createStudent(createRequest))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void createStudent_withInvalidUUID_throwsBusinessException() {
        createRequest.setId("invalid-uuid");

        StepVerifier.create(studentService.createStudent(createRequest))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void createStudent_error() {
        when(studentRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(studentService.createStudent(createRequest))
                .expectError(DatabaseException.class)
                .verify();
    }

    @Test
    void getStudentById_success() {
        when(studentRepository.findById(testId)).thenReturn(Mono.just(student));

        StepVerifier.create(studentService.getStudentById(testId.toString()))
                .expectNext(StudentDTO.fromEntity(student))
                .verifyComplete();
    }

    @Test
    void getStudentById_notFound() {
        UUID notFoundId = UUID.randomUUID();
        when(studentRepository.findById(notFoundId)).thenReturn(Mono.empty());

        StepVerifier.create(studentService.getStudentById(notFoundId.toString()))
                .expectError(DatabaseException.class)
                .verify();
    }

    @Test
    void getStudentById_invalidUUID_throwsBusinessException() {
        StepVerifier.create(studentService.getStudentById("invalid-uuid"))
                .expectError(BusinessException.class)
                .verify();
    }
}