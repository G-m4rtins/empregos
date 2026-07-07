package com.gabriel.empregos.core.services.jwt;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.gabriel.empregos.config.JwtConfigProperties;
import com.gabriel.empregos.core.exceptions.JwtServiceException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JjwtJwtService implements JwtService {

    private final JwtConfigProperties jwtConfigProperties;

    @Override
    public String generateAccessToken(String sub) {
        return generateToken(sub, jwtConfigProperties.getAccessSecret(), jwtConfigProperties.getAccessExpiresIn());
    }

    @Override
    public String getSubFromAccessToken(String token) {
        var key = Keys.hmacShaKeyFor(jwtConfigProperties.getAccessSecret().getBytes());

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new JwtServiceException("Invalid JWT token");
        }

    }

    @Override
    public String generateRefreshToken(String sub) {
        return generateToken(sub, jwtConfigProperties.getRefreshSecret(), jwtConfigProperties.getRefreshExpiresIn());
    }

    @Override
    public String getSubFromRefreshToken(String token) {
        return getSubFromToken(token, jwtConfigProperties.getRefreshSecret());
    }

    private String generateToken(String sub, String secret, Long expiresIn) {
        var now = Instant.now();
        var expiration = now.plusMillis(expiresIn);
        var key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(sub)
                .issuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

    private String getSubFromToken(String token, String secret) {
        var key = Keys.hmacShaKeyFor(secret.getBytes());

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new JwtServiceException("Invalid JWT token");
        }
    }

}
