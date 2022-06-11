package org.tonberry.calories.calorieserver.utilities;

import lombok.NonNull;
import org.tonberry.calories.calorieserver.filter.CookieAuthenticationFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Stream;

public class Cookies {
    public static Optional<String> decodeCookie(Optional<Cookie> cookie) {
        return cookie.map((c -> Crypto.hashSha256(Crypto.decodeBase64(c.getValue()))));
    }

    public static Optional<Cookie> getCookie(@NonNull HttpServletRequest request, String cookieName) {
        return Stream.of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst();
    }
}
