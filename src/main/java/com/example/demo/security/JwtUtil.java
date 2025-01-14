package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "/rddUvlDamKlwk9qJySuPsptC4PyUMpBiYrBpepWjP3mGH6EhvRegvcfuQ/8HWGQO32vX0V7LHbVn9K2av/JV9uVMLiq9DSX3+fSgdu1PK7LxzJ0sOXk1KbyftRyHxvqs7fTPSJmjJO2sJod/9tfo3IncWsbfMCjZrlLraTqwkSriRg/O/5QW3pv8ruWx5WuAB1Zx/p0V1rcBsU4UzS/Qn40V8lD67KRGeuIwRPVgWslEyDYV1TLfKlCEpSMGEEUFumth+LeU1NPYi8J5dSY6kaiJiOAkwHTczum/Osh4S9MojIDZQQv1wqJQ3Fj2PODxq5wqlkTeRMby2vf5v07crNIY8vPDblnHSed32xoj/g="; // החלף במפתח סודי אמיתי

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractUsername(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
