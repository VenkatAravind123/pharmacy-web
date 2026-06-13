package com.pharmacy.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.Customizer.withDefaults;





@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
  
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            org.springframework.security.config.annotation.web.builders.HttpSecurity http)
            throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categories/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/categories/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/categories/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/categories/**"
                        ).hasRole("ADMIN")
                        
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/medicines/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/medicines/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/medicines/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/medicines/**"
                        ).hasRole("ADMIN")
                        
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/inventory/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/inventory/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/inventory/**"
                        ).hasRole("ADMIN")
                        
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders"
                        ).hasRole("ADMIN")
                        
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/*/status"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders/**"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/orders/**"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/**"
                        ).hasRole("CUSTOMER")
                        
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/prescriptions/**"
                        ).hasRole("CUSTOMER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/prescriptions/**"
                        ).hasRole("ADMIN")
                        
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/prescriptions/**"
                        ).hasRole("ADMIN")
                        
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .httpBasic(withDefaults())

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}