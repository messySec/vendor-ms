package com.github.Hugornda.vendor_ms.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String ADMIN_USERNAME = "admin";
    private static final String USER_ROLE = "USER";
    private static final String USER_USERNAME = "user";
    private final VendorAccessDeniedHandler vendorAccessDeniedHandler;

    @Value("${app.admin-password}")
    private String adminPassword;

    @Value("${app.user-password}")
    private String userPassword;

    public SecurityConfig(VendorAccessDeniedHandler vendorAccessDeniedHandler) {
        this.vendorAccessDeniedHandler = vendorAccessDeniedHandler;
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        String encodedAdminPassword = passwordEncoder().encode(adminPassword);
        String encodedUserPassword = passwordEncoder().encode(userPassword);
        UserDetails adminUser = User.builder()
                .username(ADMIN_USERNAME)
                .password(encodedAdminPassword)
                .roles(ADMIN_ROLE)
                .build();

        UserDetails defaultUser = User.builder()
                .username(USER_USERNAME)
                .password(encodedUserPassword)
                .roles(USER_ROLE)
                .build();

        return new MapReactiveUserDetailsService(adminUser, defaultUser);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF
            .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                    .pathMatchers("/graphql").authenticated() // Auth only required for the graphql requests
                    .anyExchange().permitAll())
            .exceptionHandling(exceptionHandlingSpec ->
                    exceptionHandlingSpec.accessDeniedHandler(vendorAccessDeniedHandler))
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
