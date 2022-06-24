package org.tonberry.calories.calorieserver.controllers.graphql;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.tonberry.calories.calorieserver.persistence.auth.User;

import java.security.Principal;

public class GraphQLControllerBase {
    protected User getUser(Principal principal) {
        return (User)((AbstractAuthenticationToken) principal).getPrincipal();
    }
}
