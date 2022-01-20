package com.bcp.challange.exchangerateservice.api;

import com.bcp.challange.exchangerateservice.domain.entity.Exchange;
import com.bcp.challange.exchangerateservice.domain.response.ExchangeCurrencyResponse;
import com.bcp.challange.exchangerateservice.repository.ExchangeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ExchangeApi {

    private final ExchangeRepository exchangeRepository;

    public ExchangeApi(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @GetMapping("/exchange")
    public Mono<Void> getExchange(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().setLocation(URI.create("/exchange/USD"));
        return response.setComplete();
    }

    @GetMapping("/exchange/{symbol}")
    public Mono<ResponseEntity<ExchangeCurrencyResponse>> getExchangeBySymbol(@PathVariable String symbol) {
        return exchangeRepository
                .findAll()
                .collectList()
                .filter(exchanges -> exchanges.stream().anyMatch(ex -> ex.getQuoteAsset().equals(symbol)
                        || ex.getBaseAsset().equals(symbol)))
                .map(exchanges -> {
                    if(symbol.equals("USD")) {
                        return new ExchangeCurrencyResponse(String.valueOf(System.currentTimeMillis()), symbol,
                                exchanges.stream().collect(Collectors.toMap(Exchange::getQuoteAsset, Exchange::getRate)));
                    } else {
                        Exchange exchange = exchanges.stream().filter(ex -> ex.getQuoteAsset().equals(symbol)).findAny().orElseThrow();
                        Map<String, BigDecimal> rates = exchanges.stream().collect(Collectors
                                .toMap(ex -> ex.getQuoteAsset().equals(symbol) ? ex.getBaseAsset() : ex.getQuoteAsset(),
                                        ex -> ex.getQuoteAsset().equals(symbol) ? BigDecimal.ONE.divide(ex.getRate(), 5, RoundingMode.HALF_UP)
                                                : ex.getRate().divide(exchange.getRate(), 5, RoundingMode.HALF_UP)));
                        return new ExchangeCurrencyResponse(String.valueOf(System.currentTimeMillis()), symbol, rates);
                    }
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
