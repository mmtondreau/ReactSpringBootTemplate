package org.tonberry.calories.calorieserver.filter;

import lombok.NonNull;
import org.apache.tomcat.util.codec.binary.Base64;
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

import static org.tonberry.calories.calorieserver.controllers.AuthController.COOKIE_NAME;

@Component
public class CookieAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private MyUserDetailsService userDetailsService;

    private Optional<String> getCookie(@NonNull HttpServletRequest request) {
        return Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .map((c -> Crypto.hashSha256(Crypto.decodeBase64(c.getValue()))));
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        Optional<String> sessionToken = getCookie(request);

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

    private Optional<String> decodeCookie(Optional<Cookie> cookie) {
        return cookie.map((c -> Crypto.hashSha256(new String(Base64.decodeBase64(c.getValue())))));
    }
}
