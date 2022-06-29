package org.tonberry.calories.calorieserver.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.services.UserService;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationStartup {

    private final UserService userService;
    @Value("${admin.password}")
    String adminPassword;
    @Value("${admin.username}")
    String adminUsername;
    @Value("${admin.apiKey:#{null}}")
    String adminAPIKey;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void applicationReadyEvent() {
        if (userService.findByUsername(adminUsername).isEmpty()) {
            Authority authority = userService.createAuthority("ROLE_ADMIN");
            Role role = userService.createRole("admin");
            userService.addAuthorityToRole(authority, role);
            User user = userService.createUser(adminUsername, adminPassword);
            userService.addRoleToUser(role, user);
        }

    }
}
