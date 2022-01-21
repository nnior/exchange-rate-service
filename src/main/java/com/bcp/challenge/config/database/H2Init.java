package com.bcp.challenge.config.database;

import com.bcp.challenge.domain.entity.Exchange;
import com.bcp.challenge.repository.CurrencyRepository;
import com.bcp.challenge.repository.ExchangeRepository;
import com.bcp.challenge.util.Constants;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class H2Init {

    @Bean
    ApplicationRunner init(CurrencyRepository currencyRepository, ExchangeRepository exchangeRepository) {
        return args -> currencyRepository.findAll()
                .filter(currency -> !currency.getSymbol().equals(Constants.BASE_TICKET))
                .map(currency -> new Exchange(null, Constants.BASE_TICKET, currency.getSymbol(),
                        BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0.0, 5.0 + 1.0))
                                .setScale(5, RoundingMode.HALF_UP)))
                .collectList().subscribe(exchanges -> exchangeRepository.saveAll(exchanges).then().subscribe());
    }


}
