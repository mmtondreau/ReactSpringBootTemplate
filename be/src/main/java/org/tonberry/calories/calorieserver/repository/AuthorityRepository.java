package org.tonberry.calories.calorieserver.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorityRepository extends R2dbcRepository<Authority, Long> {
    Mono<Authority> findByName(String name);

    @Query(value = """
                SELECT a.*
                FROM auth.authorities a
                JOIN auth.role_authorities ra  ON a.authority_id=ra.authority_id
                JOIN auth.roles r ON r.role_id=ra.role_id
                JOIN auth.user_roles ur ON r.role_id=ur.role_id
                JOIN auth.users u ON u.user_id=ur.user_id
                WHERE u.user_id = :user_id
            """)
    Flux<Authority> findAuthoritiesByUserId(@Param("user_id") long userId);
}
