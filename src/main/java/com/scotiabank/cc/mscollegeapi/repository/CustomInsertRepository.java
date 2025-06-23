package com.scotiabank.cc.mscollegeapi.repository;

import reactor.core.publisher.Mono;

public interface CustomInsertRepository<T> {
  Mono<T> insertWithCustomId(T entity, Class<T> entityClass);
}