package com.bcp.challange.exchangerateservice.domain.response;

import lombok.Data;

@Data
public class ConvertResponse {
    private Double monto;
    private Double montoTipoCambio;
    private String monedaOrigen;
    private String monedaDestino;
    private Double tipoCambio;
}
