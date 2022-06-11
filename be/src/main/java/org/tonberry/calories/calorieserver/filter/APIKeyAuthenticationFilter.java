package org.tonberry.calories.calorieserver.filter;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tonberry.calories.calorieserver.security.MyUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.tonberry.calories.calorieserver.controllers.AuthController.COOKIE_NAME;

@Component
public class APIKeyAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private MyUserDetailsService userDetailsService;

    private static final String BEARER_PREFIX = "Bearer ";

    private String getAPIKey(@NonNull HttpServletRequest request) {
        String apiKey = null;
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
         apiKey = authorizationHeader.substring(BEARER_PREFIX.length());
        }
        return apiKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String apiKey = getAPIKey(request);

        if (apiKey != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByAPIKey(apiKey);
            if (userDetails != null) {
                PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                preAuthenticatedAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(preAuthenticatedAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
