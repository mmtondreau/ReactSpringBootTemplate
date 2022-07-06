package org.tonberry.calories.calorieserver.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tonberry.calories.calorieserver.persistence.auth.*;
import org.tonberry.calories.calorieserver.repository.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleAuthoritiesRepository roleAuthoritiesRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> findByUsername(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<Authority> createAuthority(@NonNull String name) {
        Authority authority = Authority.builder()
                .withName(name)
                .build();
        return authorityRepository.save(authority);
    }

    public Mono<Role> createRole(@NonNull String name) {
        Role role = Role.builder().withName(name).build();
        return roleRepository.save(role);

    }

    public Mono<User> createUser(@NonNull String username, @NonNull CharSequence password) {
        User user = User.builder()
                .withUsername(username)
                .withPassword(passwordEncoder.encode(password))
                .withAccountNonExpired(true)
                .withAccountNonLocked(true)
                .withCredentialsNonExpired(true)
                .withEnabled(true)
                .build();
        return userRepository.save(user);
    }

    public Mono<RoleAuthority> addAuthorityToRole(@NonNull Authority authority, @NonNull Role role) {
        RoleAuthority roleAuthority = RoleAuthority.builder()
                .withAuthorityId(authority.getAuthorityId())
                .withRoleId(role.getRoleId())
                .build();
        return roleAuthoritiesRepository.save(roleAuthority);
    }

    public Mono<UserRole> addRoleToUser(@NonNull Role role, @NonNull User user) {
        UserRole userRole = UserRole.builder()
                .withRoleId(role.getRoleId())
                .withUserId(user.getUserId())
                .build();
        return userRoleRepository.save(userRole);
    }

    public Flux<Authority> getAuthoritiesForUser(@NonNull User user) {
        return authorityRepository.findAuthoritiesByUserId(user.getUserId());
    }

}
