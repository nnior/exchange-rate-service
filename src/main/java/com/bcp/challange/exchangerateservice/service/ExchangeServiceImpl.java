package com.bcp.challange.exchangerateservice.service;

import com.bcp.challange.exchangerateservice.domain.entity.Exchange;
import com.bcp.challange.exchangerateservice.domain.request.ConvertRequest;
import com.bcp.challange.exchangerateservice.domain.request.UpdateExchangeRequest;
import com.bcp.challange.exchangerateservice.domain.response.ConvertResponse;
import com.bcp.challange.exchangerateservice.domain.response.ExchangeCurrencyResponse;
import com.bcp.challange.exchangerateservice.repository.ExchangeRepository;
import com.bcp.challange.exchangerateservice.service.interfaces.ExchangeService;
import com.bcp.challange.exchangerateservice.util.Constants;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeRepository exchangeRepository;

    public ExchangeServiceImpl(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public Mono<ExchangeCurrencyResponse> getExchangeBySymbol(String symbol) {
        return exchangeRepository
                .findAll()
                .collectList()
                .filter(exchanges -> exchanges.stream().anyMatch(ex -> ex.getQuoteAsset().equals(symbol)
                        || ex.getBaseAsset().equals(symbol)))
                .map(exchanges -> {
                    if(symbol.equals(Constants.BASE_TICKET)) {
                        return new ExchangeCurrencyResponse(String.valueOf(System.currentTimeMillis()), symbol,
                                exchanges.stream().collect(Collectors.toMap(Exchange::getQuoteAsset, Exchange::getRate)));
                    } else {
                        Exchange exchange = exchanges.stream().filter(ex -> ex.getQuoteAsset().equals(symbol)).findAny().orElseThrow();
                        Map<String, BigDecimal> rates = exchanges.stream().collect(Collectors
                                .toMap(ex -> ex.getQuoteAsset().equals(symbol) ? ex.getBaseAsset() : ex.getQuoteAsset(),
                                        ex -> ex.getQuoteAsset().equals(symbol) ? BigDecimal.ONE.divide(ex.getRate(), 5, RoundingMode.HALF_UP)
                                                : ex.getRate().divide(exchange.getRate(), 5, RoundingMode.HALF_UP)));
                        return new ExchangeCurrencyResponse(String.valueOf(System.currentTimeMillis()), symbol, rates);
                    }
                });
    }

    @Override
    public Mono<Exchange> updateExchangeRate(Mono<UpdateExchangeRequest> request) {
        return request
            .flatMap(r -> exchangeRepository.findByQuoteAsset(r.getMoneda())
                    .doOnNext(e -> e.setRate(BigDecimal.valueOf(r.getMonto()))))
            .flatMap(exchangeRepository::save);
    }

    @Override
    public Mono<ConvertResponse> convertCurrency(Mono<ConvertRequest> request) {
        return request
                .flatMap(req -> {
                    Mono<Exchange> fromCurrency = req.getMonedaOrigen().equals(Constants.BASE_TICKET)
                            ? Mono.just(new Exchange(null, null, null, BigDecimal.valueOf(1)))
                            : exchangeRepository.findByQuoteAsset(req.getMonedaOrigen());
                    Mono<Exchange> toCurrency = req.getMonedaDestino().equals(Constants.BASE_TICKET)
                            ? Mono.just(new Exchange(null, null, null, BigDecimal.valueOf(1)))
                            : exchangeRepository.findByQuoteAsset(req.getMonedaDestino());
                    return Mono.zip(fromCurrency, toCurrency).map(exchange -> {
                        BigDecimal tipoCambio = exchange.getT2().getRate().divide(exchange.getT1().getRate(), 5, RoundingMode.HALF_UP);
                        ConvertResponse response = new ConvertResponse();
                        response.setMonedaOrigen(req.getMonedaOrigen());
                        response.setMonedaDestino(req.getMonedaDestino());
                        response.setMonto(req.getMonto());
                        response.setMontoTipoCambio(tipoCambio.multiply(BigDecimal.valueOf(req.getMonto())).doubleValue());
                        response.setTipoCambio(tipoCambio.doubleValue());
                        return response;
                    });
                });
    }

}
