package com.scotiabank.cc.mscollegeapi.repository;

import com.scotiabank.cc.mscollegeapi.entity.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, String> {
}
