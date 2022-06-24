package org.tonberry.calories.calorieserver.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.tonberry.calories.calorieserver.config.security.MyUserDetailsService;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.persistence.redis.AuthSession;
import org.tonberry.calories.calorieserver.repository.AuthSessionRepository;
import org.tonberry.calories.calorieserver.utilities.Crypto;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final AuthSessionRepository authSessionRepository;

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public Optional<String> authenticate(@NonNull String username, @NonNull String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            return Optional.empty();
        }
        String sessionToken = UUID.randomUUID().toString();
        final User userDetails = (User) userDetailsService.loadUserByUsername(username);
        AuthSession authSession = AuthSession.builder()
                .withUserId(userDetails.getUserId())
                .withExpiration(addHoursToJavaUtilDate(new Date(), 24))
                .withId(Crypto.hashSha256(sessionToken))
                .build();
        authSessionRepository.save(authSession);
        return Optional.ofNullable(sessionToken);
    }

    public void deauthorize(@NonNull String sessionToken) {
        authSessionRepository.deleteById(sessionToken);
    }
}
