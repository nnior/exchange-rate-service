package com.bcp.challange.exchangerateservice.service.interfaces;

import com.bcp.challange.exchangerateservice.domain.entity.Exchange;
import com.bcp.challange.exchangerateservice.domain.request.ConvertRequest;
import com.bcp.challange.exchangerateservice.domain.request.UpdateExchangeRequest;
import com.bcp.challange.exchangerateservice.domain.response.ConvertResponse;
import com.bcp.challange.exchangerateservice.domain.response.ExchangeCurrencyResponse;
import reactor.core.publisher.Mono;

public interface ExchangeService {
    Mono<ExchangeCurrencyResponse> getExchangeBySymbol(String symbol);
    Mono<Exchange> updateExchangeRate(Mono<UpdateExchangeRequest> request);
    Mono<ConvertResponse> convertCurrency(Mono<ConvertRequest> request);
}
