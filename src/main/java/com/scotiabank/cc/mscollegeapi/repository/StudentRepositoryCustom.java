package com.scotiabank.cc.mscollegeapi.repository;

import com.scotiabank.cc.mscollegeapi.entity.Student;
import reactor.core.publisher.Mono;

public interface StudentRepositoryCustom {
  Mono<Student> insertWithCustomId(Student student);
}