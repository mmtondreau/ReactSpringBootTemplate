package org.tonberry.calories.calorieserver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.tonberry.calories.calorieserver.persistence.auth.AuthSession;

import java.util.Optional;

public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {
    Optional<AuthSession> findByToken(String token);
}
