package com.bcp.challenge.service;

import com.bcp.challenge.domain.entity.Currency;
import com.bcp.challenge.repository.CurrencyRepository;
import com.bcp.challenge.service.interfaces.CurrencyService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Flux<Currency> getCurrency() {
        return currencyRepository.findAll();
    }

    @Override
    public Mono<Currency> getCurrencyBySymbol(String symbol) {
        return currencyRepository.findBySymbol(symbol);
    }

}
