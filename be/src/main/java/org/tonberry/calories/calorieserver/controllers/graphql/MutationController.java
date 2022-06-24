package org.tonberry.calories.calorieserver.controllers.graphql;


import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.tonberry.calories.calorieserver.filter.CookieAuthenticationFilter;
import org.tonberry.calories.calorieserver.services.AuthService;
import org.tonberry.calories.calorieserver.utilities.CookieService;
import org.tonberry.graphql.Types;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MutationController {

    private final AuthService authService;
    private final CookieService cookieService;
    private final HttpServletResponse servletResponse;
    private final HttpServletRequest servletRequest;

    @MutationMapping
    public boolean login(@Argument(name = "input") Types.LoginInput input) {
        Optional<String> sessionTokenOpt = authService.authenticate(input.getUsername(), input.getPassword());
        return sessionTokenOpt.map(sessionToken -> {
            Cookie authCookie = cookieService.createSessionCookie(sessionToken);
            servletResponse.addCookie(authCookie);
            return true;
        }).orElse(false);
    }

    @MutationMapping
    public boolean logout() {
        Optional<Cookie> cookieOpt = CookieService.getCookie(servletRequest, CookieAuthenticationFilter.COOKIE_NAME);
        cookieOpt.ifPresent((cookie -> {
            cookieService.resetCookie(cookie);
            servletResponse.addCookie(cookie);
        }));
        CookieService.decodeCookie(cookieOpt).ifPresent(authService::deauthorize);
        return true;
    }
}