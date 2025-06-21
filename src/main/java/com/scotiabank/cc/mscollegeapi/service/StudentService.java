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

    public Mono<StudentDTO> createStudent(StudentDTO student) {
        Student studentEntity = new Student();
        studentEntity.setName(student.getName());
        studentEntity.setLastName(student.getLastName());
        studentEntity.setAge(student.getAge());
        studentEntity.setStatus(student.getStatus().getValue());
        Mono<Student> newStudent = studentRepository.save(studentEntity);
        return newStudent.map(StudentDTO::fromEntity)
                .doOnError(throwable -> {
                    log.error("Error creating student: {}", throwable.getMessage());
                })
                .onErrorResume(throwable ->
                        Mono.error(new DatabaseException("Error creating student")));

    }
}
