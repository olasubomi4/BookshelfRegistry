package com.group5.bookshelfregistry.security;


import com.group5.bookshelfregistry.repositories.UserRepository;
import com.group5.bookshelfregistry.security.filters.AuthenticationFilter;
import com.group5.bookshelfregistry.security.filters.ExceptionHandlerFilter;
import com.group5.bookshelfregistry.security.filters.JWTAuthorizationFilter;
import com.group5.bookshelfregistry.security.manager.CustomAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Value("${jwt.secret.key}")
    public String secretKey;
    @Value("${jwt.token.expiration}")
    public Integer tokenExpiration;
    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;
    @Autowired
    private RequestMappingHandlerMapping reqMap;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(secretKey,tokenExpiration
                ,customAuthenticationManager,userRepository);
        authenticationFilter.setFilterProcessesUrl("/api/v1/user/authenticate");

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeRequests(authorize -> authorize.requestMatchers(HttpMethod.POST,
                                SecurityConstants.REGISTER_PATH).permitAll()
                        .requestMatchers(SecurityConstants.AUTH_WHITELIST).permitAll()
                .anyRequest()
                .authenticated())
                .addFilterBefore(new ExceptionHandlerFilter(),AuthenticationFilter.class )
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(secretKey,tokenExpiration,reqMap), AuthenticationFilter.class)
                .sessionManagement(mgt->mgt.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }


}