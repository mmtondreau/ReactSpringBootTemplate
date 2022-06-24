package org.tonberry.calories.calorieserver.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tonberry.calories.calorieserver.filter.CookieAuthenticationFilter;
import org.tonberry.calories.calorieserver.schema.AuthenticatRequest;
import org.tonberry.calories.calorieserver.schema.AuthenticateResponse;
import org.tonberry.calories.calorieserver.services.AuthService;
import org.tonberry.calories.calorieserver.utilities.CookieService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;
    private final CookieService cookieService;

    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticatRequest authenticatRequest, HttpServletResponse servletResponse) throws Exception {
        Optional<String> sessionTokenOpt = authService.authenticate(authenticatRequest.getUsername(), authenticatRequest.getPassword());
        return sessionTokenOpt.map(sessionToken -> {
            Cookie authCookie = cookieService.createSessionCookie(sessionToken);
            servletResponse.addCookie(authCookie);
            return ResponseEntity.ok(new AuthenticateResponse("Success"));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new AuthenticateResponse("Failure"))
        );

    }

    @RequestMapping(value = "/v1/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Optional<Cookie> cookieOpt = CookieService.getCookie(servletRequest, CookieAuthenticationFilter.COOKIE_NAME);
        cookieOpt.ifPresent((cookie -> {
            cookieService.resetCookie(cookie);
            servletResponse.addCookie(cookie);
        }));
        CookieService.decodeCookie(cookieOpt).ifPresent(authService::deauthorize);
        return ResponseEntity.ok(new AuthenticateResponse("Success"));
    }


}
