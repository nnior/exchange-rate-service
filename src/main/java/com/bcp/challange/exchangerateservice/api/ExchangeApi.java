package com.bcp.challange.exchangerateservice.api;

import com.bcp.challange.exchangerateservice.domain.request.UpdateExchangeRequest;
import com.bcp.challange.exchangerateservice.domain.response.BasicResponse;
import com.bcp.challange.exchangerateservice.domain.response.ExchangeCurrencyResponse;
import com.bcp.challange.exchangerateservice.domain.response.error.ErrorResponse;
import com.bcp.challange.exchangerateservice.service.interfaces.ExchangeService;
import com.bcp.challange.exchangerateservice.util.Constants;
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
