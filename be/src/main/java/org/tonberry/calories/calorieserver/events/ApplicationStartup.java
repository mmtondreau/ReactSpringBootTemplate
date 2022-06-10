package org.tonberry.calories.calorieserver.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import org.tonberry.calories.calorieserver.persistence.auth.RoleAuthorities;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.UserRepository;

@Component
@Slf4j
public class ApplicationStartup {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyEvent() {
        if (userRepository.findByUsername("bootstrap").isEmpty()) {

            Authority authority = Authority.builder()
                    .withName("graphql")
                    .build();

            RoleAuthorities roleAuthorities = RoleAuthorities.builder()
                    .withAuthority(authority)
                    .build();

            Role role = Role.builder().withName("admin").build()
                    .addRoleAuthority(roleAuthorities);

            User user = User.builder()
                    .withUsername("bootstrap")
                    .withApiKey("5D728295-24D0-4EDB-8FE3-A075CFFF1207")
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
