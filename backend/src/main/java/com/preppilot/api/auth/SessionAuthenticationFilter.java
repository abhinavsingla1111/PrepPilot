package com.preppilot.api.auth;

import com.preppilot.api.config.AppProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.util.Arrays;
import java.util.Collections;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final AuthSessionRepository sessionRepository;
    private final TokenHasher tokenHasher;
    private final Clock clock;
    private final String cookieName;

    public SessionAuthenticationFilter(
            AuthSessionRepository sessionRepository,
            TokenHasher tokenHasher,
            Clock clock,
            AppProperties properties
    ) {
        this.sessionRepository = sessionRepository;
        this.tokenHasher = tokenHasher;
        this.clock = clock;
        this.cookieName = properties.auth().cookie().name();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            findCookie(request).flatMap(rawToken -> sessionRepository
                            .findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(tokenHasher.hash(rawToken), clock.instant()))
                    .ifPresent(session -> {
                        PrepPilotPrincipal principal = new PrepPilotPrincipal(
                                session.getUser().getId(),
                                session.getUser().getEmail()
                        );
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }
        filterChain.doFilter(request, response);
    }

    private java.util.Optional<String> findCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return java.util.Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(value -> !value.isBlank())
                .findFirst();
    }
}
