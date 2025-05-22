package com.cochalla.cochalla.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
    private Key key;
    private final long TOKEN_VALID_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret}")
    private String secretKeyString;

    @PostConstruct
    public void init(){
        try{
            key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        } catch (Exception e){
            throw new IllegalStateException("JWT 비밀키 로딩 실패", e);
        }
        
    }

    public String createToken(String userId){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + TOKEN_VALID_TIME);

        return Jwts.builder()
        .setSubject(userId)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
    }

    public String getUserIdFromToken(String token){
        return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
