package com.scotiabank.cc.mscollegeapi.service;

import com.scotiabank.cc.mscollegeapi.dto.StudentDTO;
import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.exception.DatabaseException;
import com.scotiabank.cc.mscollegeapi.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Flux<StudentDTO> listStudents() {
        return studentRepository.findAll().map(StudentDTO::fromEntity);
    }

    public Mono<StudentDTO> createStudent(StudentDTO request) {
        Student student = new Student();
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
