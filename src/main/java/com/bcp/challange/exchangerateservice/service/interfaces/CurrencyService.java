package com.bcp.challange.exchangerateservice.service.interfaces;

import com.bcp.challange.exchangerateservice.domain.entity.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
    Flux<Currency> getCurrency();
    Mono<Currency> getCurrencyBySymbol(String symbol);
}
