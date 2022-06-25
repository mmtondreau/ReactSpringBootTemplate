package org.tonberry.calories.calorieserver.controllers.graphql;


import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.tonberry.calories.calorieserver.services.AuthService;
import org.tonberry.graphql.Types;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class MutationController {

    private final AuthService authService;

    @MutationMapping
    public Mono<Boolean> login(@Argument(name = "input") Types.LoginInput input) {
        return  Mono.just(authService.authenticate(input.getUsername(), input.getPassword()));
    }

    @MutationMapping
    public Mono<Boolean> logout() {
        authService.deauthorize();
        return Mono.just(true);
    }
}