package com.scotiabank.cc.mscollegeapi.repository.impl;

import com.scotiabank.cc.mscollegeapi.entity.Student;
import com.scotiabank.cc.mscollegeapi.repository.StudentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {

  private final R2dbcEntityTemplate r2dbcEntityTemplate;

  @Override
  public Mono<Student> insertWithCustomId(Student student) {
    return r2dbcEntityTemplate.insert(Student.class)
        .using(student)
        .thenReturn(student);
  }
}