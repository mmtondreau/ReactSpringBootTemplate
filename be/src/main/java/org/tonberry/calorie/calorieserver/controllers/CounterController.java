package org.tonberry.calorie.calorieserver.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CounterController {
    private static int count = 0;

    @QueryMapping
    public int count() {
        return count;
    }

    @MutationMapping
    public Integer setCount(@Argument("count") Integer count) {
        CounterController.count = count;
        return count;
    }

    @MutationMapping
    public Integer decrementCount() {
        return --CounterController.count;
    }

    @MutationMapping
    public Integer incrementCount() {
        return ++CounterController.count;
    }
}
