package org.tonberry.calories.calorieserver.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthRestController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Mono<CsrfToken> login(ServerWebExchange serverWebExchange, Principal principal) {
        if (principal == null) {
            return Mono.error(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid username or password"));
        }
        return serverWebExchange.getAttribute(CsrfToken.class.getName());
    }


}
