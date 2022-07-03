package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository extends R2dbcRepository<Role, Long> {
    Mono<Role> findByName(String name);
}
