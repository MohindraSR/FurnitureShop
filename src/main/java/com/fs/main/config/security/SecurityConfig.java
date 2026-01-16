package com.fs.main.config.security;

import com.fs.main.config.jwt.JwtAuthEntryPoint;
import com.fs.main.config.jwt.JwtFilter;
import com.fs.main.config.passwordEncoder.PasswordEncoderConfig;
import com.fs.main.service.CustomerService;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @Autowired
    private CustomerService customerService;

    private final PasswordEncoderConfig passwordEncoder;
    public SecurityConfig(JwtFilter jwtFilter, PasswordEncoderConfig passwordEncoder) {
        this.jwtFilter = jwtFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF because JWT is stateless
                .csrf(csrf -> csrf.disable())

                .userDetailsService((UserDetailsService) customerService)

                // Do not create or use HTTP sessions
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request,
                                                   response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers(
                                "/login",
                                "/checkLoginCredential",
                                "/customerRegistrationPre",
                                "/customerRegistrationPost",
                                "/api/auth/**",
                                "/api/user/**",
                                "/user/**",
                                "/css/**",
                                "/js/**"
                        ).permitAll()                // Public endpoints
                        .anyRequest().authenticated() // JWT required
                )

                // Add JWT filter before Spring Security filter
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    /*@Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService((UserDetailsService) customerService)
                .passwordEncoder(passwordEncoder.passwordEncoder());

        return authenticationManagerBuilder.build();
    }*/

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}