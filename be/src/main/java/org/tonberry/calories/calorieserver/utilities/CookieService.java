package org.tonberry.calories.calorieserver.utilities;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tonberry.calories.calorieserver.filter.CookieAuthenticationFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class CookieService {

    @Value("${auth.cookie.secure}")
    public Boolean secureCookie;
    @Value("${auth.cookie.httpOnly}")
    public Boolean httpOnly;

    public void resetCookie(Cookie cookie) {
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secureCookie);
        cookie.setMaxAge(0);
        cookie.setPath("/");
    }

    public Cookie createSessionCookie(String sessionToken) {
        Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, Crypto.encodeBase64(sessionToken));
        authCookie.setHttpOnly(httpOnly);
        authCookie.setSecure(secureCookie);
        authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
        authCookie.setPath("/");
        return authCookie;
    }

    public static Optional<String> decodeCookie(Optional<Cookie> cookie) {
        return cookie.map((c -> Crypto.hashSha256(Crypto.decodeBase64(c.getValue()))));
    }

    public static Optional<Cookie> getCookie(@NonNull HttpServletRequest request, String cookieName) {
        return Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst();
    }
}
