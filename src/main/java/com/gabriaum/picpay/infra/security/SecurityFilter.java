package com.gabriaum.picpay.infra.security;

import com.gabriaum.picpay.infra.security.generator.TokenGenerator;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorizationHeader.substring(7);
            if (!tokenGenerator.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String email = tokenGenerator.getEmailByToken(token);
            if (email == null || email.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Optional<UserEntity> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}