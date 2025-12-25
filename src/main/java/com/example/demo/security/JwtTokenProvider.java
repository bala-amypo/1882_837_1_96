package com.example.demo.security;

import com.example.demo.model.UserAccount;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String jwtSecret = "base64EncodedSecretKeyForDemoPurposeOnly12345678901234567890";
    private final long jwtExpirationInMs = 3600000;

    public String generateToken(UserAccount user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) { return false; }
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
    }

    // REQUIRED BY TESTS
    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(token).getBody();
        return Long.valueOf(claims.get("userId").toString());
    }

    // REQUIRED BY TESTS
    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecret.getBytes()).build().parseClaimsJws(token).getBody();
        return claims.get("role").toString();
    }
}