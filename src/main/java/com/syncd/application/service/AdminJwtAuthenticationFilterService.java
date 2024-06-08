package com.syncd.application.service;

import com.syncd.application.port.in.admin.GetAdminIdFromTokenUsecase;
import com.syncd.application.port.in.ResolveTokenUsecase;
import com.syncd.application.port.in.ValidateTokenUsecase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Service
@Primary
@RequiredArgsConstructor
public class AdminJwtAuthenticationFilterService extends OncePerRequestFilter {

    private final ResolveTokenUsecase resolveTokenUsecase;
    private final ValidateTokenUsecase validateTokenUsecase;
    private final GetAdminIdFromTokenUsecase getAdminIdFromTokenUsecase;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveTokenUsecase.resolveToken(request);
        if (token != null && validateTokenUsecase.validateToken(token)) {
            System.out.println("Admin JWT Token is valid: " + token);
            UsernamePasswordAuthenticationToken auth = getAuthentication(token);
            if (auth != null) {
                System.out.println("Admin Authentication set for user: " + auth.getName());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                System.out.println("Admin Authentication is null");
            }
        } else {
            System.out.println("Admin JWT Token is invalid or missing.");
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String adminId = getAdminIdFromTokenUsecase.getAdminIdFromToken(token); // JWT에서 어드민 ID 추출
        if (adminId != null) {
            return new UsernamePasswordAuthenticationToken(adminId, null, new ArrayList<>()); // 필요한 권한 정보 추가 가능
        }
        return null;
    }
}
