package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "/rddUvlDamKlwk9qJySuPsptC4PyUMpBiYrBpepWjP3mGH6EhvRegvcfuQ/8HWGQO32vX0V7LHbVn9K2av/JV9uVMLiq9DSX3+fSgdu1PK7LxzJ0sOXk1KbyftRyHxvqs7fTPSJmjJO2sJod/9tfo3IncWsbfMCjZrlLraTqwkSriRg/O/5QW3pv8ruWx5WuAB1Zx/p0V1rcBsU4UzS/Qn40V8lD67KRGeuIwRPVgWslEyDYV1TLfKlCEpSMGEEUFumth+LeU1NPYi8J5dSY6kaiJiOAkwHTczum/Osh4S9MojIDZQQv1wqJQ3Fj2PODxq5wqlkTeRMby2vf5v07crNIY8vPDblnHSed32xoj/g="; // החלף במפתח סודי אמיתי

    // שליפת ה-email מהטוקן
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }


    // שליפת Claim מסוים מתוך הטוקן
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // שליפת כל ה-Claims מתוך הטוקן
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

    // יצירת טוקן על בסיס email
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // הגדרת email כ-Subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // זמן יצירה
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // זמן תפוגה (10 שעות)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // חתימה עם אלגוריתם ומפתח סודי
                .compact();
    }

    // בדיקת תקפות הטוקן
    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractUsername(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    // בדיקת האם הטוקן פג תוקף
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // שליפת תאריך תפוגה מתוך הטוקן
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
