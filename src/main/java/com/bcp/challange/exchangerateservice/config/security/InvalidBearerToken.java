package com.bcp.challange.exchangerateservice.config.security;

import org.springframework.security.core.AuthenticationException;

public class InvalidBearerToken extends AuthenticationException {
    public InvalidBearerToken(String msg) {
        super(msg);
    }
}
