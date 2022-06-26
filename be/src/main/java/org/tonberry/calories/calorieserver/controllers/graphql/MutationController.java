package org.tonberry.calories.calorieserver.controllers.graphql;


import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MutationController {


    @MutationMapping
    public Mono<String> login(Principal principal) {
        return  Mono.just(principal.getName());
    }
}