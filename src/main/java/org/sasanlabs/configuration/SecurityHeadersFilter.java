package org.sasanlabs.configuration;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Sets the response headers that have to be present on every response to be worth anything.
 *
 * <p>Framing protection in particular cannot be a per-endpoint decision. The clickjacking levels
 * set the header on the JSON their handler returns, but what a browser actually frames is the
 * level's HTML page, served straight off the static resource handler, which no handler code
 * touches. A page that can be framed is a page that can be clickjacked whatever the API response
 * says, so {@code X-Frame-Options} and the {@code frame-ancestors} directive that supersedes it are
 * applied here, to everything.
 *
 * <p>{@code nosniff} goes with them: several levels reflect caller-controlled content, and without
 * it a browser may decide a response is HTML regardless of the declared content type.
 *
 * <p>This filter must be the <em>only</em> place these headers are set. It runs before the handler,
 * and a handler that adds its own value has it appended rather than replaced — and a browser that
 * is given {@code X-Frame-Options} more than once ignores the header completely, which turns
 * framing protection into no framing protection at all. The clickjacking handlers used to set it as
 * well and every one of their responses carried it twice. Setting it here also covers the responses
 * no handler ever produces: a request with the wrong method, an OPTIONS probe, an error page, and
 * the level's own HTML served straight off the static resource handler. An attacker frames a URL,
 * not a handler.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class SecurityHeadersFilter implements Filter {

    private static final String X_FRAME_OPTIONS = "X-Frame-Options";
    private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    private static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
    private static final String REFERRER_POLICY = "Referrer-Policy";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader(X_FRAME_OPTIONS, "DENY");
            httpResponse.setHeader(CONTENT_SECURITY_POLICY, "frame-ancestors 'none'");
            httpResponse.setHeader(X_CONTENT_TYPE_OPTIONS, "nosniff");
            httpResponse.setHeader(REFERRER_POLICY, "no-referrer");
        }
        chain.doFilter(request, response);
    }
}
