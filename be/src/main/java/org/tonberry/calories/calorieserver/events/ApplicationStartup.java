package org.tonberry.calories.calorieserver.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.UserRepository;
import org.tonberry.calories.calorieserver.services.UserService;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationStartup {


    private final UserRepository userRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.password}")
    String adminPassword;
    @Value("${admin.username}")
    String adminUsername;
    @Value("${admin.apiKey:#{null}}")
    String adminAPIKey;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyEvent() {
        if (userService.findByUsername(adminUsername).block() == null) {
            log.info("Creating admin [%s]".formatted(adminUsername));
            Mono<Role> role = userService.createRole("admin");
            Mono<Authority> authority = userService.createAuthority("ROLE_ADMIN");
            Mono<User> user = userService.createUser(adminUsername, adminPassword);
            Mono.zip(role, authority, user).flatMap(result -> Mono.zip(
                    userService.addRoleToUser(result.getT1(), result.getT3()),
                    userService.addAuthorityToRole(result.getT2(), result.getT1()))
            ).block();
        }
    }
}
