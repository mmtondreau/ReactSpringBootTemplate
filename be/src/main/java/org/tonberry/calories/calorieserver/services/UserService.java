package org.tonberry.calories.calorieserver.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tonberry.calories.calorieserver.persistence.auth.*;
import org.tonberry.calories.calorieserver.repository.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final RoleAuthoritiesRepository roleAuthoritiesRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

    public Authority createAuthority(@NonNull String name) {
        Authority authority = Authority.builder()
                .withName(name)
                .build();
        authorityRepository.save(authority);
        return authority;
    }

    public Role createRole(@NonNull String name) {
        Role role = Role.builder().withName("admin").build();
        roleRepository.save(role);
        return role;
    }

    public User createUser(@NonNull String username, @NonNull CharSequence password) {
        User user = User.builder()
                .withUsername(username)
                .withPassword(passwordEncoder.encode(password))
                .withAccountNonExpired(true)
                .withAccountNonLocked(true)
                .withCredentialsNonExpired(true)
                .withEnabled(true)
                .build();
        userRepository.save(user);
        return user;
    }

    public void addAuthorityToRole(@NonNull Authority authority, @NonNull Role role) {
        RoleAuthority roleAuthority = RoleAuthority.builder()
                .withAuthority(authority)
                .build();
        role.addRoleAuthority(roleAuthority);
        roleRepository.save(role);
    }

    public void addRoleToUser(@NonNull Role role, @NonNull User user) {
        UserRole userRole = UserRole.builder()
                .withRole(role)
                .build();
        user.addUserRole(userRole);
        userRepository.save(user);
    }


}
