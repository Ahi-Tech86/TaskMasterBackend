package org.schizoscript.backend.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider userAuthenticationProvider;

    /**
     * First we get Header "Authorization", then if Header value is not null, we get substring by index of 7. Then we are
     *   trying to get context from SecurityContextHolder and setting authentication with userAuthProvider we validate
     *   jwtToken.
     *
     * SecurityContextHolder is parts of Spring Security which service for saving security context. It provides access to
     *   current auth object, which represents the current user's details, like username or a list of roles. When we get
     *   context, we get data about authentication of user. When we clear context, we delete this data and user being
     *   unauthorized.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null) {
            String jwtToken = authHeader.substring(7);

            try {
                SecurityContextHolder.getContext().setAuthentication(userAuthenticationProvider.tokenIsValid(jwtToken));
            } catch (RuntimeException exception) {
                SecurityContextHolder.clearContext();
                throw exception;
            }
        }

        filterChain.doFilter(request, response);
    }
}
