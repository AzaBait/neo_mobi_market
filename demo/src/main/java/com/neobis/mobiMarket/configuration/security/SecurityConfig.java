package com.neobis.mobiMarket.configuration.security;

import com.neobis.mobiMarket.configuration.security.jwt.JwtTokenFilter;
import com.neobis.mobiMarket.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserServiceImpl userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("*"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/register",
                                "/error",
                                "/api/auth",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/user/**",
                                "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product/list",
                                "api/product/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/product/save").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/product/*/like").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/image/uploadImage").permitAll()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public DaoAuthenticationProvider daoauthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
