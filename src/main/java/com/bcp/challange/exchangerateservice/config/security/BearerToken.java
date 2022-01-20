package com.bcp.challange.exchangerateservice.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Objects;

public class BearerToken extends AbstractAuthenticationToken {

    private final String value;

    public BearerToken(String value) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object getCredentials() {
        return this.getValue();
    }

    @Override
    public Object getPrincipal() {
        return this.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BearerToken)) return false;
        if (!super.equals(o)) return false;
        BearerToken that = (BearerToken) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

}
