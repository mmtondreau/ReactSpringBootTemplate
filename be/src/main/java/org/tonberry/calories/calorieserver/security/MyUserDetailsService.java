package org.tonberry.calories.calorieserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.AuthSessionRepository;
import org.tonberry.calories.calorieserver.repository.UserRepository;


import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthSessionRepository authSessionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetails = userRepository.findByUsername(username);
        return userDetails.orElse(null);
    }

    public UserDetails loadUserByAPIKey(String apiKey) throws UsernameNotFoundException {
        Optional<User> userDetails = userRepository.findByApiKey(apiKey);
        return userDetails.orElse(null);
    }

    public UserDetails loadUserBySessionToken(String sessionToken) throws UsernameNotFoundException {
        return authSessionRepository.findByToken(sessionToken)
                .flatMap(session -> userRepository.findById(session.getUserId()))
                .orElse(null);
    }
}
