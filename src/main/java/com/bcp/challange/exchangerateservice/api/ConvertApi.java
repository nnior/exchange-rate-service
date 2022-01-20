package com.bcp.challange.exchangerateservice.api;

import com.bcp.challange.exchangerateservice.domain.request.ConvertRequest;
import com.bcp.challange.exchangerateservice.domain.response.BasicResponse;
import com.bcp.challange.exchangerateservice.domain.response.error.ErrorResponse;
import com.bcp.challange.exchangerateservice.service.interfaces.ExchangeService;
import com.bcp.challange.exchangerateservice.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class ConvertApi {

    private final ExchangeService exchangeService;

    public ConvertApi(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("/convert")
    public Mono<ResponseEntity<BasicResponse>> convert(@Valid @RequestBody Mono<ConvertRequest> request) {
        return exchangeService
            .convertCurrency(request)
            .map(req -> ResponseEntity.ok().body(BasicResponse.builder()
                .message(Constants.RESPONSE_CONVERTER_SUCCESS_MESSAGE)
                .result(req)
                .build()))
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .onErrorResume(WebExchangeBindException.class, ex -> Mono.just(ResponseEntity.badRequest().body(
                BasicResponse.builder().message(Constants.RESPONSE_INVALID_REQUEST_ERROR_MESSAGE)
                    .errors(ex.getFieldErrors().stream()
                        .map(fieldError -> new ErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList()))
                    .build())));
    }

}
