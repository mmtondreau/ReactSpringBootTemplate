package org.tonberry.calories.calorieserver.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tonberry.calories.calorieserver.persistence.auth.AuthSession;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.AuthSessionRepository;
import org.tonberry.calories.calorieserver.schema.AuthenticatRequest;
import org.tonberry.calories.calorieserver.schema.AuthenticateResponse;
import org.tonberry.calories.calorieserver.security.MyUserDetailsService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    AuthSessionRepository authSessionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticatRequest authenticatRequest, HttpServletResponse servletResponse) throws Exception {
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
                .withExpiration( addHoursToJavaUtilDate(new Date(), 24))
                .withToken(sessionToken)
                .build();
        authSessionRepository.save(authSession);

        Cookie authCookie = new Cookie("SESSION", sessionToken);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");
        servletResponse.addCookie(authCookie);

        return ResponseEntity.ok(new AuthenticateResponse("Success"));
    }

}
