package com.gabriel.empregos.core.services.jwt;

public interface JwtService {

    String generateAccessToken(String sub);
    String getSubFromAccessToken(String token);

}
