package com.project.keyboard.config;

import com.project.keyboard.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final String SECRET_KEY = "mySuperSecretKeyForJWTSigning123!";

    private static final long ACCESS_TOKEN_VALIDITY_USER = 60 * 60 * 1000; // 1 giờ
    private static final long REFRESH_TOKEN_VALIDITY_USER = 7 * 24 * 60 * 60 * 1000; // 7 ngày

    private static final long ACCESS_TOKEN_VALIDITY_ADMIN = 60 * 60 * 1000;
//    private static final long ACCESS_TOKEN_VALIDITY_ADMIN = 30 * 60 * 1000;// 30 phút
    private static final long REFRESH_TOKEN_VALIDITY_ADMIN = 2 * 24 * 60 * 60 * 1000; // 2 ngày

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken(Users user) {
        long now = System.currentTimeMillis();
        long expiry = user.isAdmin() ? ACCESS_TOKEN_VALIDITY_ADMIN : ACCESS_TOKEN_VALIDITY_USER;

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("isAdmin", user.isAdmin())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Users user) {
        long now = System.currentTimeMillis();
        long expiry = user.isAdmin() ? REFRESH_TOKEN_VALIDITY_ADMIN : REFRESH_TOKEN_VALIDITY_USER;

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("isAdmin", user.isAdmin())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
