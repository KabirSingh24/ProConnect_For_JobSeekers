package com.example.backend.security;

import com.example.backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtService {

    @Value("${jwt.secretKey}")
    public String jwtSecretKey;

    public SecretKey getSecretKey(){return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));}

    public String generateToken(User user){
        return Jwts.builder()
                .claim("userid",user.getId())
                .signWith(getSecretKey())
                .issuedAt(new Date(System.currentTimeMillis()))
                .subject(user.getUsername())
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
}
