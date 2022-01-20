package com.bcp.challange.exchangerateservice.config.database;

import com.bcp.challange.exchangerateservice.domain.entity.Exchange;
import com.bcp.challange.exchangerateservice.repository.CurrencyRepository;
import com.bcp.challange.exchangerateservice.repository.ExchangeRepository;
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
                .filter(currency -> !currency.getSymbol().equals("USD"))
                .map(currency -> new Exchange(null, "USD", currency.getSymbol(),
                        BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0.0, 5.0 + 1.0))
                                .setScale(5, RoundingMode.HALF_UP)))
                .collectList().subscribe(exchanges -> exchangeRepository.saveAll(exchanges).then().subscribe());
    }


}
