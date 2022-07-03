package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.tonberry.calories.calorieserver.persistence.auth.UserRole;

public interface UserRoleRepository extends R2dbcRepository<UserRole, Long> {
}
