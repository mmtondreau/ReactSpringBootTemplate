package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tonberry.calories.calorieserver.persistence.auth.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByApiKey(String apiKey);
}
