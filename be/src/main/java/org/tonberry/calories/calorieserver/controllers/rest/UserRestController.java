package org.tonberry.calories.calorieserver.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tonberry.calories.calorieserver.filter.CookieAuthenticationFilter;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import org.tonberry.calories.calorieserver.persistence.auth.RoleAuthorities;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.AuthorityRepository;
import org.tonberry.calories.calorieserver.repository.RoleRepository;
import org.tonberry.calories.calorieserver.repository.UserRepository;
import org.tonberry.calories.calorieserver.schema.AuthenticateResponse;
import org.tonberry.calories.calorieserver.schema.user.CreateRoleRequest;
import org.tonberry.calories.calorieserver.schema.user.CreateUserRequest;
import org.tonberry.calories.calorieserver.services.AuthService;
import org.tonberry.calories.calorieserver.utilities.CookieService;
import org.tonberry.calories.calorieserver.utilities.Crypto;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/")
public class UserRestController {


    @Value("${auth.cookie.secure}")
    public Boolean secureCookie;
    @Value("${auth.cookie.httpOnly}")
    public Boolean httpOnly;
    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;
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
