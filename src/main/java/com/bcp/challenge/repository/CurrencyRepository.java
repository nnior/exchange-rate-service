package com.bcp.challenge.repository;

import com.bcp.challenge.domain.entity.Currency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CurrencyRepository extends ReactiveCrudRepository<Currency, Long> {

    Mono<Currency> findBySymbol(String symbol);

}