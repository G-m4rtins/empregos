package com.gabriel.empregos.core.exceptions;

public class JwtServiceException extends RuntimeException {

    public JwtServiceException(String message) {
        super(message);
    }

}
