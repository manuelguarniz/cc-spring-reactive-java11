package com.scotiabank.cc.mscollegeapi.service;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.exception.BusinessException;
import com.scotiabank.cc.mscollegeapi.exception.DatabaseException;
import com.scotiabank.cc.mscollegeapi.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
            return studentRepository.findById(studentId)
                    .hasElement()
                    .flatMap(exists -> {
                        if (Boolean.TRUE.equals(exists)) {
                            String errorMessage = String.format(
                                    "No se puede crear un estudiante con el ID '%s' porque ya existe", studentId);
                            log.warn("Business rule violation: {}", errorMessage);
                            return Mono.error(new BusinessException(errorMessage));
                        } else {
                            return createNewStudent(request, studentId);
                        }
                    });
        } else {
            String generatedId = UUID.randomUUID().toString();
            return createNewStudent(request, generatedId);
        }
    }

    private Mono<StudentDTO> createNewStudent(StudentDTO request, String studentId) {
        Student student = new Student();
        student.setId(studentId);
        student.setName(request.getName().trim());
        student.setLastName(request.getLastName().trim());
        student.setAge(request.getAge());
        student.setStatus(request.getStatus().getValue());

        return studentRepository.save(student)
                .map(StudentDTO::fromEntity)
                .doOnSuccess(s -> log.info("Student created successfully: {}", s.getId()))
                .doOnError(e -> log.error("Error creating student: {}", e.getMessage()))
                .onErrorResume(throwable -> Mono
                        .error(new DatabaseException("Error creating student: " + throwable.getMessage())));
    }

}
