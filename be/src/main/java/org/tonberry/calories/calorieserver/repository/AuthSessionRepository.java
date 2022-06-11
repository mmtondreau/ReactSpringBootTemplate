package org.tonberry.calories.calorieserver.repository;


import org.springframework.data.repository.CrudRepository;
import org.tonberry.calories.calorieserver.persistence.redis.AuthSession;


public interface AuthSessionRepository extends CrudRepository<AuthSession, String> {
}
