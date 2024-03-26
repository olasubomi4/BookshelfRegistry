package com.group5.bookshelfregistry.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.group5.bookshelfregistry.annotations.NoAuth;
import com.group5.bookshelfregistry.security.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret.key}")
    public String secretKey;
    @Value("${jwt.token.expiration}")
    public Integer tokenExpiration;

    private RequestMappingHandlerMapping reqMap;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        try {
            if (!isAuthRequired(request)) {
                grantAccessHasViewer(filterChain, request, response);
                return;
            }
        } catch (Exception e) {
            logger.error(e);
        }

        if (header == null || !header.startsWith(SecurityConstants.BEARER)) {
                filterChain.doFilter(request, response);
                return;
        }
        String token = header.replace(SecurityConstants.BEARER, "");
        List<String> roles= JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token)
                .getClaim("roles").asList(String.class);

        String user = JWT.require(Algorithm.HMAC512(secretKey))
            .build()
            .verify(token)
            .getSubject();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(roles.stream()
                .map( SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,authorities );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean isAuthRequired(HttpServletRequest request) throws Exception {
        HandlerExecutionChain handlerExecutionChain=reqMap.getHandler(request);
        if(handlerExecutionChain==null) {
            return false;}

        HandlerMethod method = (HandlerMethod) handlerExecutionChain.getHandler();
        return !method.getMethod().isAnnotationPresent(NoAuth.class);
    }

    private void grantAccessHasViewer(FilterChain filterChain,HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken("viewer", null,
                Arrays.asList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}
