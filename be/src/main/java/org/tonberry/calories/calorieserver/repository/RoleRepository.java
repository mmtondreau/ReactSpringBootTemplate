package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tonberry.calories.calorieserver.persistence.auth.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
