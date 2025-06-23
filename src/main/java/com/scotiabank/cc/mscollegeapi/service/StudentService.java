package com.scotiabank.cc.mscollegeapi.service;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.exception.BusinessException;
import com.scotiabank.cc.mscollegeapi.exception.DatabaseException;
import com.scotiabank.cc.mscollegeapi.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Flux<StudentDTO> listStudents() {
        return studentRepository.findAll().map(StudentDTO::fromEntity);
    }

    public Mono<StudentDTO> createStudent(StudentDTO request) {
        String studentId = request.getId();

        if (!Strings.isBlank(studentId)) {
            try {
                UUID uuid = UUID.fromString(studentId);
                return studentRepository.findById(uuid)
                        .hasElement()
                        .flatMap(exists -> {
                            if (Boolean.TRUE.equals(exists)) {
                                String errorMessage = String.format(
                                        "No se puede crear un estudiante con el ID '%s' porque ya existe", studentId);
                                log.warn("Business rule violation: {}", errorMessage);
                                return Mono.error(new BusinessException(errorMessage, HttpStatus.BAD_REQUEST));
                            } else {
                                return createNewStudentWithCustomId(request, uuid);
                            }
                        });
            } catch (IllegalArgumentException e) {
                String errorMessage = String.format("El ID '%s' no es un UUID válido", studentId);
                log.warn("Invalid UUID format: {}", studentId);
                return Mono.error(new BusinessException(errorMessage));
            }
        } else {
            return createNewStudentWithGeneratedId(request);
        }
    }

    private Mono<StudentDTO> createNewStudentWithCustomId(StudentDTO request, UUID studentId) {
        Student student = new Student();
        student.setId(studentId);
        student.setName(request.getName().trim());
        student.setLastName(request.getLastName().trim());
        student.setAge(request.getAge());
        student.setStatus(request.getStatus().getValue());

        return studentRepository.insertWithCustomId(student, Student.class)
                .map(StudentDTO::fromEntity)
                .doOnSuccess(s -> log.info("Student created successfully with custom ID: {}", s.getId()))
                .doOnError(e -> log.error("Error creating student with custom ID: {}", e.getMessage()))
                .onErrorResume(throwable -> Mono
                        .error(new DatabaseException("Error creating student: " + throwable.getMessage())));
    }

    private Mono<StudentDTO> createNewStudentWithGeneratedId(StudentDTO request) {
        Student student = new Student();
        student.setName(request.getName().trim());
        student.setLastName(request.getLastName().trim());
        student.setAge(request.getAge());
        student.setStatus(request.getStatus().getValue());

        return studentRepository.save(student)
                .map(StudentDTO::fromEntity)
                .doOnSuccess(s -> log.info("Student created successfully with generated ID: {}", s.getId()))
                .doOnError(e -> log.error("Error creating student: {}", e.getMessage()))
                .onErrorResume(throwable -> Mono
                        .error(new DatabaseException("Error creating student: " + throwable.getMessage())));
    }

    public Mono<StudentDTO> getStudentById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return studentRepository.findById(uuid)
                    .map(StudentDTO::fromEntity)
                    .switchIfEmpty(Mono.error(new DatabaseException("Student not found with id: " + id)))
                    .doOnSuccess(s -> log.info("Student retrieved successfully: {}", s.getId()))
                    .doOnError(e -> log.error("Error retrieving student: {}", e.getMessage()));
        } catch (IllegalArgumentException e) {
            String errorMessage = String.format("El ID '%s' no es un UUID válido", id);
            log.warn("Invalid UUID format: {}", id);
            return Mono.error(new BusinessException(errorMessage));
        }
    }
}
