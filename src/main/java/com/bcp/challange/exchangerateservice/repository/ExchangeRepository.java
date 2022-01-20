package com.bcp.challange.exchangerateservice.repository;

import com.bcp.challange.exchangerateservice.domain.entity.Exchange;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ExchangeRepository extends ReactiveCrudRepository<Exchange, Long> {

    Mono<Exchange> findByQuoteAsset(String quote);

}