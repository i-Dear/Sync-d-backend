package com.syncd.adapter.in.oauth;

import com.syncd.domain.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

public class JwtUtils {

    private static String accessTokenSecretKey;

    public static String generateToken(User user) {

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
}
