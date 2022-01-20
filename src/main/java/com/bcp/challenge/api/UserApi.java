package com.bcp.challenge.api;

import com.bcp.challenge.domain.request.LoginRequest;
import com.bcp.challenge.domain.response.JwtResponse;
import com.bcp.challenge.config.security.JwtSupport;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
public class UserApi {

    private final ReactiveUserDetailsService users;
    private final PasswordEncoder encoder;
    private final JwtSupport jwtSupport;

    public UserApi(ReactiveUserDetailsService users, PasswordEncoder encoder, JwtSupport jwtSupport) {
        this.users = users;
        this.encoder = encoder;
        this.jwtSupport = jwtSupport;
    }

    @PostMapping("/login")
    public Mono<JwtResponse> login(@RequestBody LoginRequest request) {
        return users.findByUsername(request.getUsername())
            .filter(user -> encoder.matches(request.getPassword(), user.getPassword()))
            .map(user -> new JwtResponse(jwtSupport.generate(user.getUsername()).getValue()))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)));

    }

}
