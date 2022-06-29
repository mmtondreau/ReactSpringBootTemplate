package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tonberry.calories.calorieserver.persistence.auth.RoleAuthority;

public interface RoleAuthoritiesRepository extends JpaRepository<RoleAuthority, Long> {
}
