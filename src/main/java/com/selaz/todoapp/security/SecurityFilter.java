package com.selaz.todoapp.security;

import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.exceptions.ResourceNotFoundException;
import com.selaz.todoapp.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = TokenService.getTokenFromRequest(request);
        if (token.isPresent()) {
            String username = tokenService.validateToken(token.get());
            Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);

            if (optionalUser.isEmpty()) {
                throw new ResourceNotFoundException("User with this username not found");
            }

            User user = optionalUser.get();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
