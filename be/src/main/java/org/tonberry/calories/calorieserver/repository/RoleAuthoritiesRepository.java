package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.tonberry.calories.calorieserver.persistence.auth.RoleAuthority;

public interface RoleAuthoritiesRepository extends R2dbcRepository<RoleAuthority, Long> {
}
