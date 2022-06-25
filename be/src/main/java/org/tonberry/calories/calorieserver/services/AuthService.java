package org.tonberry.calories.calorieserver.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ReactiveAuthenticationManager authenticationManager;

    public Boolean authenticate(@NonNull String username, @NonNull String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            ).subscribe(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
        } catch (BadCredentialsException e) {
            return false;
        }
        return true;
    }

    public void deauthorize() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
