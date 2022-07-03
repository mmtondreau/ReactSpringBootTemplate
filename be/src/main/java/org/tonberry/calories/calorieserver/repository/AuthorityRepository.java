package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import reactor.core.publisher.Mono;

public interface AuthorityRepository extends R2dbcRepository<Authority, Long> {
    Mono<Authority> findByName(String name);
}
