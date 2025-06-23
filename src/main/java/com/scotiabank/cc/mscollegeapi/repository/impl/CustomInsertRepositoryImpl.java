package com.scotiabank.cc.mscollegeapi.repository.impl;

import com.scotiabank.cc.mscollegeapi.repository.CustomInsertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CustomInsertRepositoryImpl<T> implements CustomInsertRepository<T> {

  private final R2dbcEntityTemplate r2dbcEntityTemplate;

  @Override
  public Mono<T> insertWithCustomId(T entity, Class<T> entityClass) {
    return r2dbcEntityTemplate.insert(entityClass)
        .using(entity)
        .thenReturn(entity);
  }
}