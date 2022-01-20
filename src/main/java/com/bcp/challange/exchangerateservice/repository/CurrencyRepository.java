package com.bcp.challange.exchangerateservice.repository;

import com.bcp.challange.exchangerateservice.domain.entity.Currency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CurrencyRepository extends ReactiveCrudRepository<Currency, Long> {

    Mono<Currency> findBySymbol(String symbol);

}