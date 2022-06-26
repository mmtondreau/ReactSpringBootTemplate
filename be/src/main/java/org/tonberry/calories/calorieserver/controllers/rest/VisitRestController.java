package org.tonberry.calories.calorieserver.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class VisitRestController {

    @RequestMapping(value = "/csrf", method = RequestMethod.GET)
    public Mono<CsrfToken> csrf(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getAttribute(CsrfToken.class.getName());
    }
}
