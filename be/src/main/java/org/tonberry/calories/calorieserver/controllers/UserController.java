package org.tonberry.calories.calorieserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import org.tonberry.calories.calorieserver.persistence.auth.RoleAuthorities;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.AuthorityRepository;
import org.tonberry.calories.calorieserver.repository.RoleRepository;
import org.tonberry.calories.calorieserver.repository.UserRepository;
import org.tonberry.calories.calorieserver.schema.user.CreateRoleRequest;
import org.tonberry.calories.calorieserver.schema.user.CreateUserRequest;

import javax.annotation.security.RolesAllowed;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @RolesAllowed({"ROLE_ADMIN"})
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public void createUser(@RequestBody CreateUserRequest request) {
        User user = User.builder()
                .withUsername(request.getUsername())
                .withApiKey(request.getApiKey())
                .withPassword(passwordEncoder.encode(request.getPassword()))
                .withAccountNonExpired(true)
                .withAccountNonLocked(true)
                .withCredentialsNonExpired(true)
                .withEnabled(true)
                .withRole(roleRepository.findByName(request.getRole()).orElseThrow())
                .build();
        userRepository.save(user);
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public void createRole(@RequestBody CreateRoleRequest request) {
        Role role = Role.builder()
                        .withName(request.getName())
                        .build();
        request.getAuthorities().forEach(authorityName -> {
            Authority authority = authorityRepository.findByName(authorityName)
                    .orElseGet(() -> Authority.builder()
                            .withName(authorityName)
                            .build());
            RoleAuthorities roleAuthorities = RoleAuthorities.builder()
                    .withRole(role)
                    .withAuthority(authority)
                    .build();
            role.getRoleAuthorities().add(roleAuthorities);
        });
        roleRepository.save(role);
    }
}
