package org.tonberry.calories.calorieserver.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tonberry.calories.calorieserver.persistence.auth.User;
import org.tonberry.calories.calorieserver.repository.UserRepository;
import org.tonberry.calories.calorieserver.services.UserService;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username).flatMap(this::loadAuthorities).map(user -> user);
    }

    private Mono<User> loadAuthorities(User user) {
        return userService.getAuthoritiesForUser(user)
                .log()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collectList()
                .map(user::setAuthorities);
    }
}