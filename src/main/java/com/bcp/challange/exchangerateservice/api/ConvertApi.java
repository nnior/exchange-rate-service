package com.bcp.challange.exchangerateservice.api;

import com.bcp.challange.exchangerateservice.domain.entity.Exchange;
import com.bcp.challange.exchangerateservice.domain.request.ConvertRequest;
import com.bcp.challange.exchangerateservice.domain.response.BasicResponse;
import com.bcp.challange.exchangerateservice.domain.response.ConvertResponse;
import com.bcp.challange.exchangerateservice.domain.response.error.ErrorResponse;
import com.bcp.challange.exchangerateservice.repository.ExchangeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@RestController
public class ConvertApi {

    private final ExchangeRepository exchangeRepository;

    public ConvertApi(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @PostMapping("/convert")
    public Mono<ResponseEntity<BasicResponse>> updateExchangeRate(@Valid @RequestBody Mono<ConvertRequest> request) {
        return request
                .flatMap(req -> {
                    Mono<Exchange> fromCurrency = req.getMonedaOrigen().equals("USD")
                            ? Mono.just(new Exchange(null, null, null, BigDecimal.valueOf(1)))
                            : exchangeRepository.findByQuoteAsset(req.getMonedaOrigen());
                    Mono<Exchange> toCurrency = req.getMonedaDestino().equals("USD")
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
                })
                .map(req -> ResponseEntity.ok().body(BasicResponse.builder()
                        .message("Conversion exitosa.")
                        .result(req)
                        .build()))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(WebExchangeBindException.class, ex -> Mono.just(ResponseEntity.badRequest().body(
                    BasicResponse.builder().message("La peticion no tiene los valores correctos.")
                    .errors(ex.getFieldErrors().stream()
                            .map(fieldError -> new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                            .collect(Collectors.toList()))
                    .build())));
    }

}
