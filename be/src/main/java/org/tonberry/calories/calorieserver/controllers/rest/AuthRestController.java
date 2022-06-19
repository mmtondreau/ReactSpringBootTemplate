package org.tonberry.calories.calorieserver.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.tonberry.calories.calorieserver.filter.CookieAuthenticationFilter;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.persistence.redis.AuthSession;
import org.tonberry.calories.calorieserver.repository.AuthSessionRepository;
import org.tonberry.calories.calorieserver.schema.AuthenticatRequest;
import org.tonberry.calories.calorieserver.schema.AuthenticateResponse;
import org.tonberry.calories.calorieserver.config.security.MyUserDetailsService;
import org.tonberry.calories.calorieserver.services.AuthService;
import org.tonberry.calories.calorieserver.utilities.Cookies;
import org.tonberry.calories.calorieserver.utilities.Crypto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    @Value("${auth.cookie.secure}")
    public Boolean secureCookie;
    @Value("${auth.cookie.httpOnly}")
    public Boolean httpOnly;

    private final AuthService authService;

    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticatRequest authenticatRequest, HttpServletResponse servletResponse) throws Exception {
        String sessionToken = authService.authenticate(authenticatRequest.getUsername(), authenticatRequest.getPassword());
        Cookie authCookie = createSessionCookie(sessionToken);
        servletResponse.addCookie(authCookie);
        return ResponseEntity.ok(new AuthenticateResponse("Success"));
    }

    private Cookie createSessionCookie(String sessionToken) {
        Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, Crypto.encodeBase64(sessionToken));
        authCookie.setHttpOnly(httpOnly);
        authCookie.setSecure(secureCookie);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");
        return authCookie;
    }

    @RequestMapping(value = "/v1/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Optional<Cookie> cookieOpt = Cookies.getCookie(servletRequest, CookieAuthenticationFilter.COOKIE_NAME);
        cookieOpt.ifPresent((cookie -> {
            resetCookie(cookie);
            servletResponse.addCookie(cookie);
        }));
        Cookies.decodeCookie(cookieOpt).ifPresent(authService::deauthorize);
        return ResponseEntity.ok(new AuthenticateResponse("Success"));
    }

    public void resetCookie(Cookie cookie) {
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secureCookie);
        cookie.setMaxAge(0);
        cookie.setPath("/");
    }

}
