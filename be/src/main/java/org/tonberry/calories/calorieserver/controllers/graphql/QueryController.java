package org.tonberry.calories.calorieserver.controllers.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.tonberry.calories.calorieserver.persistence.auth.Authority;
import org.tonberry.calories.calorieserver.persistence.auth.Role;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.UserRepository;
import org.tonberry.calories.calorieserver.services.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class QueryController extends GraphQLControllerBase {
    private final UserRepository userRepository;
    private final UserService userService;

    @QueryMapping
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public Mono<User> user(@Argument("username") String username) {
        return userRepository.findByUsername(username);
    }

    @QueryMapping
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public Mono<User> currentUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        User userSessioless = getUser(principal);
        return userRepository.findById(userSessioless.getUserId());
    }

    @QueryMapping
    public Mono<String> ping() {
        return Mono.just("pong");
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<String> adminPing() {
        return Mono.just("pong");
    }

    @QueryMapping
    public Mono<Role> findRoleById(@Argument("id") String id) {
        return userService.findRoleById(Long.parseLong(id));
    }

    @QueryMapping
    public Mono<Authority> findAuthorityById(@Argument("id") String id) {
        return userService.findAuthorityById(Long.parseLong(id));
    }

    @QueryMapping
    public Mono<User> findUserById(@Argument("id") String id) {
        return userService.findUserById(Long.parseLong(id));
    }

    @QueryMapping
    public Flux<Role> roles() {
        return userService.findAllRoles();
    }

    @QueryMapping
    public Flux<Authority> authorities() {
        return userService.findAllAuthorities();
    }

    @QueryMapping
    public Flux<User> users() {
        return userService.findAllUsers();
    }
}
