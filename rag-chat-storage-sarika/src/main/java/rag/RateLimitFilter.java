package rag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class RateLimitFilter extends OncePerRequestFilter {

    // Allow X requests per second (configurable)
    private final RateLimiter limiter;

    // Default constructor for Spring Boot
    public RateLimitFilter() {
        this(10.0); // default 10 req/sec if no config provided
    }

    // Customizable constructor (used in WebConfig)
    public RateLimitFilter(double rateLimit) {
        this.limiter = RateLimiter.create(rateLimit);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        if (!limiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Try again later.");
            return;
        }

        chain.doFilter(request, response);
    }
}
