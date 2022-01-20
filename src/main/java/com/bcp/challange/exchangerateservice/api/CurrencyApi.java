package com.bcp.challange.exchangerateservice.api;

import com.bcp.challange.exchangerateservice.domain.entity.Currency;
import com.bcp.challange.exchangerateservice.repository.CurrencyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CurrencyApi {

    private final CurrencyRepository currencyRepository;

    public CurrencyApi(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/currency")
    public Flux<ResponseEntity<Currency>> getCurrency() {
        return currencyRepository.findAll().map(ResponseEntity::ok);
    }

    @GetMapping("/currency/{symbol}")
    public Mono<ResponseEntity<Currency>> getCurrencyBySymbol(@PathVariable String symbol) {
        return currencyRepository.findBySymbol(symbol)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
