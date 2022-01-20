package com.bcp.challenge.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtSupport {

    private final SecretKey key = Keys.hmacShaKeyFor("hEtLekNcH4LDXNpaY7sfE8MXaJGFa/4uiH+YPq5bLxw=".getBytes());
    private final JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

    public BearerToken generate(String username) {
        JwtBuilder builder = Jwts.builder().setSubject(username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
            .signWith(key);
        return new BearerToken(builder.compact());
    }

    public String getUsername(BearerToken token) {
        return parser.parseClaimsJws(token.getValue()).getBody().getSubject();
    }

    public Boolean isValid(BearerToken token, UserDetails user) {
        Claims claims = parser.parseClaimsJws(token.getValue()).getBody();
        boolean unexpired = claims.getExpiration().after(Date.from(Instant.now()));

        return unexpired && (Objects.equals(claims.getSubject(), user.getUsername()));
    }

}
