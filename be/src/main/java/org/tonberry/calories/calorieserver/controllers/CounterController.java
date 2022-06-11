package org.tonberry.calories.calorieserver.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import javax.annotation.security.RolesAllowed;

@Controller
public class CounterController {
    private static int count = 0;

    @QueryMapping
    public int count() {
        return count;
    }

    @MutationMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public Integer setCount(@Argument("input") Integer count) {
        CounterController.count = count;
        return count;
    }

    @MutationMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public Integer decrementCount() {
        return --CounterController.count;
    }

    @MutationMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public Integer incrementCount() {
        return ++CounterController.count;
    }
}
