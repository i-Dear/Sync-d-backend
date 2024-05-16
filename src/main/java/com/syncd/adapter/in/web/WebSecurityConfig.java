package com.syncd.adapter.in.web;

import com.syncd.application.port.in.JwtAuthenticationFilterUsecase;
import com.syncd.application.port.in.ResolveTokenUsecase;
import com.syncd.application.port.in.ValidateTokenUsecase;
import com.syncd.application.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig{

    private final ResolveTokenUsecase resolveTokenUsecase;

    private final ValidateTokenUsecase validateTokenUsecase;

    private final JwtAuthenticationFilterUsecase jwtAuthenticationFilterUsecase;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilterUsecase.JwtAuthenticationFilter(resolveTokenUsecase,validateTokenUsecase), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}