package org.tonberry.calories.calorieserver.filter;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tonberry.calories.calorieserver.security.MyUserDetailsService;
import org.tonberry.calories.calorieserver.utilities.Cookies;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {


    public static final String COOKIE_NAME = "SESSION";

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        Optional<String> sessionToken = Cookies.decodeCookie(Cookies.getCookie(request, COOKIE_NAME));

        if (sessionToken.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserBySessionToken(sessionToken.get());
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
