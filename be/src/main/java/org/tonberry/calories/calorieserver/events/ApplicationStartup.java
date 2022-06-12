package org.tonberry.calories.calorieserver.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import org.tonberry.calories.calorieserver.persistence.auth.RoleAuthorities;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.UserRepository;

import java.util.UUID;

@Component
@Slf4j
public class ApplicationStartup {

    @Value("${admin.password}")
    String adminPassword;

    @Value("${admin.username}")
    String adminUsername;

    @Value("${admin.apiKey:#{null}}")
    String adminAPIKey;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyEvent() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {

            Authority authority = Authority.builder()
                    .withName("ROLE_ADMIN")
                    .build();

            RoleAuthorities roleAuthorities = RoleAuthorities.builder()
                    .withAuthority(authority)
                    .build();

            Role role = Role.builder().withName("admin").build()
                    .addRoleAuthority(roleAuthorities);
            String apiKey = adminAPIKey == null ? UUID.randomUUID().toString() : adminAPIKey;
            User user = User.builder()
                    .withUsername(adminUsername)
                    .withApiKey(apiKey)
                    .withPassword(passwordEncoder.encode(adminPassword))
                    .withAccountNonExpired(true)
                    .withAccountNonLocked(true)
                    .withCredentialsNonExpired(true)
                    .withEnabled(true)
                    .withRole(role)
                    .build();

            userRepository.save(user);
        }
        if (userRepository.findByUsername("user").isEmpty()) {
            Authority authority = Authority.builder()
                    .withName("ROLE_USER")
                    .build();

            RoleAuthorities roleAuthorities = RoleAuthorities.builder()
                    .withAuthority(authority)
                    .build();

            Role role = Role.builder().withName("user").build()
                    .addRoleAuthority(roleAuthorities);
            User user = User.builder()
                    .withUsername("user")
                    .withApiKey("B2AEE2C5-C729-44C3-9BB3-90A26BD01B0C")
                    .withPassword(passwordEncoder.encode("password1"))
                    .withAccountNonExpired(true)
                    .withAccountNonLocked(true)
                    .withCredentialsNonExpired(true)
                    .withEnabled(true)
                    .withRole(role)
                    .build();
            userRepository.save(user);
        }

    }
}
