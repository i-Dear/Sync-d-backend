package com.syncd.application.service;
import com.syncd.application.port.in.*;
import com.syncd.domain.user.User;
import io.jsonwebtoken.*;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

@Service
@Primary
@RequiredArgsConstructor
public class JwtService implements GenerateTokenUsecase, JwtAuthenticationFilterUsecase, GetUserIdFromTokenUsecase, GetUsernameFromTokenUsecase, ResolveTokenUsecase, ValidateTokenUsecase {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.expiration}")
    private long validityInMilliseconds;
    @Value("${spring.jwt.secret}")
    private static String accessTokenSecretKey;

    @Override
    public String generateToken(User user) {

        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setIssuedAt(new Date())
                .setExpiration(createExpireDateForAccessToken())
                .signWith(SignatureAlgorithm.HS256, createSigningKey())
                .compact();
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private static Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("img", user.getProfileImg());
        return claims;
    }

    private static Date createExpireDateForAccessToken() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 60000 * 10);
        return calendar.getTime();
    }

    private static Key createSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode("aS1kZWFyLXN5bmNkLXNlY3JldC1rZXktaS1kZWFyLXN5bmNkLXNlY3JldC1rZXk=");
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

//    public String createToken(String username) {
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + validityInMilliseconds);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


//    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
//        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsernameFromToken(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 다음의 토큰 부분만 추출
        }
        return null;
    }

    @Override
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.get("id", String.class);
    }

    @Override
    public Filter JwtAuthenticationFilter(ResolveTokenUsecase resolveTokenUsecase, ValidateTokenUsecase validateTokenUsecase) {
        return new JwtAuthenticationFilterService(resolveTokenUsecase,validateTokenUsecase);
    }

//    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
//        String username = getUsernameFromToken(token);
//        User user = readUserPort.findByUsername(username);
//
//        // UserDetails의 구현체로 변환
//        UserDetails userDetails = new User();
//
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
}

