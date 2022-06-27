package org.tonberry.calories.calorieserver.config.security;

import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.WebSessionServerCsrfTokenRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CookieSessionCsrfTokenRepository extends WebSessionServerCsrfTokenRepository {

    static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";
    static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

    private String headerName = DEFAULT_CSRF_HEADER_NAME;

    private String cookiePath;
    
    private String cookieDomain;

    private String cookieName = DEFAULT_CSRF_COOKIE_NAME;

    private boolean cookieHttpOnly = true;

    private Boolean secure;

    public static CookieSessionCsrfTokenRepository withHttpOnlyFalse() {
        CookieSessionCsrfTokenRepository result = new CookieSessionCsrfTokenRepository();
        result.setCookieHttpOnly(false);
        return result;
    }

    public void setCookieHttpOnly(boolean cookieHttpOnly) {
        this.cookieHttpOnly = cookieHttpOnly;
    }

    @Override
    public Mono<Void> saveToken(ServerWebExchange exchange, CsrfToken token) {
        return super.saveToken(exchange, token).doOnSuccess((re) -> {
            String tokenValue = (token != null) ? token.getToken() : "";
            // @formatter:off
            ResponseCookie cookie = ResponseCookie.from(this.cookieName, tokenValue).domain(this.cookieDomain).httpOnly(this.cookieHttpOnly).maxAge(!tokenValue.isEmpty() ? -1 : 0).path((this.cookiePath != null) ? this.cookiePath : getRequestContext(exchange.getRequest())).secure((this.secure != null) ? this.secure : (exchange.getRequest().getSslInfo() != null)).build();
            // @formatter:on
            exchange.getResponse().addCookie(cookie);
        });
    }

    public void setCookieName(String cookieName) {
        Assert.hasLength(cookieName, "cookieName can't be null");
        this.cookieName = cookieName;
    }

    /**
     * Sets the parameter name
     *
     * @param parameterName The parameter name
     */
    public void setParameterName(String parameterName) {
        Assert.hasLength(parameterName, "parameterName can't be null");
        this.parameterName = parameterName;
    }

    /**
     * Sets the header name
     *
     * @param headerName The header name
     */
    public void setHeaderName(String headerName) {
        Assert.hasLength(headerName, "headerName can't be null");
        this.headerName = headerName;
    }

    /**
     * Sets the cookie path
     *
     * @param cookiePath The cookie path
     */
    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    /**
     * Sets the cookie domain
     *
     * @param cookieDomain The cookie domain
     */
    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    /**
     * Sets the cookie secure flag. If not set, the value depends on
     * {@link ServerHttpRequest#getSslInfo()}.
     *
     * @param secure The value for the secure flag
     * @since 5.5
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }


    private String getRequestContext(ServerHttpRequest request) {
        String contextPath = request.getPath().contextPath().value();
        return StringUtils.hasLength(contextPath) ? contextPath : "/";
    }
}
