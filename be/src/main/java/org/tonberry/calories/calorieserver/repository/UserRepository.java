package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByUsername(String username);
}
