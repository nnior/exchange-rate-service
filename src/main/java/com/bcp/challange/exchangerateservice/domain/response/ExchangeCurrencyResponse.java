package com.bcp.challange.exchangerateservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class ExchangeCurrencyResponse {

    private String timestamp;
    private String base;
    private Map<String, BigDecimal> rates;

}
