package com.group5.bookshelfregistry.security.filters;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.group5.bookshelfregistry.dto.AuthUserRequest;
import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.repositories.UserRepository;
import com.group5.bookshelfregistry.security.SecurityConstants;
import com.group5.bookshelfregistry.security.manager.CustomAuthenticationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.group5.bookshelfregistry.enums.Roles.VIEWER;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Value("${jwt.secret.key}")
    public String secretKey;
    @Value("${jwt.token.expiration}")
    public Integer tokenExpiration;

    private final CustomAuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        AuthUserRequest user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), AuthUserRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        String role= getUserRole(user);
        authorities.add(new SimpleGrantedAuthority(role));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword(),authorities);
            return authenticationManager.authenticate(authentication);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("roles",roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .sign(Algorithm.HMAC512(secretKey));
        response.addHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + token);
    }

    private String getUserRole(AuthUserRequest authUserRequest) {
      return userRepository.findByUsername(authUserRequest.getUsername()).map(User::getRole).orElse(VIEWER.getRoleName());
    }
}
