package com.bcp.challenge.config.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtSupport jwtSupport;
    private final ReactiveUserDetailsService users;

    public JwtAuthenticationManager(JwtSupport jwtSupport, ReactiveUserDetailsService users) {
        this.jwtSupport = jwtSupport;
        this.users = users;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(BearerToken.class::isInstance)
                .cast(BearerToken.class)
                .flatMap(this::validate)
                .onErrorMap( error -> new InvalidBearerToken(error.getMessage()));
    }

    private Mono<Authentication> validate(BearerToken token) {
        return users.findByUsername(jwtSupport.getUsername(token))
            .filter(user -> jwtSupport.isValid(token, user))
            .map(user -> new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword(), user.getAuthorities()))
            .cast(Authentication.class)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Token is not valid.")));
    }

}
