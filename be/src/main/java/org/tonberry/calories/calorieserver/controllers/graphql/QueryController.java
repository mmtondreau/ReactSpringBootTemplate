package org.tonberry.calories.calorieserver.controllers.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class QueryController extends GraphQLControllerBase {
    private final UserRepository userRepository;
    @QueryMapping
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public User user(@Argument("username") String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @QueryMapping
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public User currentUser(Principal principal) {
        if (principal == null) {
            return null;
        }
        User userSessioless = getUser(principal);
        return userRepository.findById(userSessioless.getUserId()).orElse(null);
    }

    @QueryMapping
    public String ping() {
        return "pong";
    }

    @QueryMapping
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    public String adminPing() {
        return "pong";
    }
}
