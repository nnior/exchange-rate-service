package com.bcp.challenge.service.interfaces;

import com.bcp.challenge.domain.entity.Exchange;
import com.bcp.challenge.domain.request.ConvertRequest;
import com.bcp.challenge.domain.request.UpdateExchangeRequest;
import com.bcp.challenge.domain.response.ExchangeCurrencyResponse;
import com.bcp.challenge.domain.response.ConvertResponse;
import reactor.core.publisher.Mono;

public interface ExchangeService {
    Mono<ExchangeCurrencyResponse> getExchangeBySymbol(String symbol);
    Mono<Exchange> updateExchangeRate(Mono<UpdateExchangeRequest> request);
    Mono<ConvertResponse> convertCurrency(Mono<ConvertRequest> request);
}
