package com.syncd.application.service;

import com.syncd.application.port.in.ResolveTokenUsecase;
import com.syncd.application.port.in.ValidateTokenUsecase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@Primary
@RequiredArgsConstructor
public class JwtAuthenticationFilterService extends OncePerRequestFilter {

    private final ResolveTokenUsecase resolveTokenUsecase;

    private final ValidateTokenUsecase validateTokenUsecase;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveTokenUsecase.resolveToken(request);
        if (token != null && validateTokenUsecase.validateToken(token)) {
            // 토큰이 유효한 경우, 사용자 인증 정보를 설정합니다.
//            UsernamePasswordAuthenticationToken auth = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}