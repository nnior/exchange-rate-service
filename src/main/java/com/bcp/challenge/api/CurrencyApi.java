package com.bcp.challenge.api;

import com.bcp.challenge.domain.entity.Currency;
import com.bcp.challenge.service.interfaces.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class CurrencyApi {

    private final CurrencyService currencyService;

    public CurrencyApi(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currency")
    public Mono<ResponseEntity<List<Currency>>> getCurrency() {
        return currencyService.getCurrency()
            .collectList()
            .map(ResponseEntity::ok);
    }

    @GetMapping("/currency/{symbol}")
    public Mono<ResponseEntity<Currency>> getCurrencyBySymbol(@PathVariable String symbol) {
        return currencyService.getCurrencyBySymbol(symbol)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
