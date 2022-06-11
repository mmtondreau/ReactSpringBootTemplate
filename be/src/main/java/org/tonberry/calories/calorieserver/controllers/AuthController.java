package org.tonberry.calories.calorieserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tonberry.calories.calorieserver.filter.CookieAuthenticationFilter;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.persistence.redis.AuthSession;
import org.tonberry.calories.calorieserver.repository.AuthSessionRepository;
import org.tonberry.calories.calorieserver.schema.AuthenticatRequest;
import org.tonberry.calories.calorieserver.schema.AuthenticateResponse;
import org.tonberry.calories.calorieserver.security.MyUserDetailsService;
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
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final AuthSessionRepository authSessionRepository;

    @Value("${auth.cookie.secure}")
    public Boolean secureCookie;
    @Value("${auth.cookie.httpOnly}")
    public Boolean httpOnly;

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticatRequest authenticatRequest, HttpServletResponse servletResponse) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticatRequest.getUsername(), authenticatRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password");
        }
        final User userDetails = (User) userDetailsService.loadUserByUsername(authenticatRequest.getUsername());
        String sessionToken = UUID.randomUUID().toString();
        AuthSession authSession = AuthSession.builder()
                .withUserId(userDetails.getUserId())
                .withExpiration(addHoursToJavaUtilDate(new Date(), 24))
                .withId(Crypto.hashSha256(sessionToken))
                .build();
        authSessionRepository.save(authSession);

        Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, Crypto.encodeBase64(sessionToken));
        authCookie.setHttpOnly(httpOnly);
        authCookie.setSecure(secureCookie);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");
        servletResponse.addCookie(authCookie);

        return ResponseEntity.ok(new AuthenticateResponse("Success"));
    }

    @RequestMapping(value = "/v1/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        Optional<Cookie> cookieOpt = Cookies.getCookie(servletRequest, CookieAuthenticationFilter.COOKIE_NAME);
        cookieOpt.ifPresent((cookie -> {
            cookie.setHttpOnly(httpOnly);
            cookie.setSecure(secureCookie);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);
        }));
        Cookies.decodeCookie(cookieOpt).ifPresent(authSessionRepository::deleteById);

        return ResponseEntity.ok(new AuthenticateResponse("Success"));
    }
}
