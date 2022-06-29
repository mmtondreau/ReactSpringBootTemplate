package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tonberry.calories.calorieserver.persistence.auth.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
