package org.tonberry.calories.calorieserver.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class SecurityConfigurer {

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(myUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain authorization(final ServerHttpSecurity http) {
        return http.csrf(csrf -> csrf.csrfTokenRepository(CookieSessionCsrfTokenRepository.withHttpOnlyFalse())).authorizeExchange(ae -> ae.anyExchange().permitAll()).httpBasic(Customizer.withDefaults()).securityContextRepository(new WebSessionServerSecurityContextRepository()).logout(logoutSpec -> logoutSpec.logoutUrl("/logout").logoutSuccessHandler(new HttpStatusReturningServerLogoutSuccessHandler(HttpStatus.OK))).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    WebFilter addCsrfToken() {
        return (exchange, next) -> Objects.requireNonNull(exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName())).doOnSuccess(token -> {
                }) // do nothing, just subscribe :/
                .then(next.filter(exchange));
    }

}
