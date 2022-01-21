package com.bcp.challenge.repository;

import com.bcp.challenge.domain.entity.Exchange;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ExchangeRepository extends ReactiveCrudRepository<Exchange, Long> {

    Mono<Exchange> findByQuoteAsset(String quote);

}