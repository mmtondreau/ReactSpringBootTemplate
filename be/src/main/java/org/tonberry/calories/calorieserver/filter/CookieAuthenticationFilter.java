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
import org.tonberry.calories.calorieserver.utilities.Crypto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {


    public static final String COOKIE_NAME = "SESSION";

    @Autowired
    private MyUserDetailsService userDetailsService;

    public static Optional<String> decodeCookie(Optional<Cookie> cookie) {
        return cookie.map((c -> Crypto.hashSha256(Crypto.decodeBase64(c.getValue()))));
    }

    public static Optional<Cookie> getCookie(@NonNull HttpServletRequest request) {
        return Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        Optional<String> sessionToken = decodeCookie(getCookie(request));

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
