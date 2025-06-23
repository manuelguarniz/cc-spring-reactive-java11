package com.scotiabank.cc.mscollegeapi.repository;

import com.scotiabank.cc.mscollegeapi.entity.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, UUID>, StudentRepositoryCustom {
}
