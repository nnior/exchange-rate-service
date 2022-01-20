package com.bcp.challenge.api;

import com.bcp.challenge.domain.request.UpdateExchangeRequest;
import com.bcp.challenge.domain.response.BasicResponse;
import com.bcp.challenge.domain.response.ExchangeCurrencyResponse;
import com.bcp.challenge.domain.response.error.ErrorResponse;
import com.bcp.challenge.service.interfaces.ExchangeService;
import com.bcp.challenge.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@RestController
public class ExchangeApi {

    private final ExchangeService exchangeService;

    public ExchangeApi(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchange")
    public Mono<Void> getExchange(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().setLocation(URI.create("/exchange/USD"));
        return response.setComplete();
    }

    @GetMapping("/exchange/{symbol}")
    public Mono<ResponseEntity<ExchangeCurrencyResponse>> getExchangeBySymbol(@PathVariable String symbol) {
        return exchangeService.getExchangeBySymbol(symbol).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/exchange")
    public Mono<ResponseEntity<BasicResponse>> updateExchangeRate(@Valid @RequestBody Mono<UpdateExchangeRequest> request) {
        return exchangeService.updateExchangeRate(request)
            .map(exchange -> ResponseEntity.ok().body(BasicResponse.builder()
                    .message(Constants.RESPONSE_UPDATE_EXCHANGE_SUCCESS_MESSAGE).build()))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume(WebExchangeBindException.class, ex -> Mono.just(ResponseEntity.badRequest().body(BasicResponse
                    .builder().message(Constants.RESPONSE_INVALID_REQUEST_ERROR_MESSAGE)
                    .errors(ex.getFieldErrors().stream()
                            .map(fieldError -> new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                            .collect(Collectors.toList()))
                    .build())));
    }

}
