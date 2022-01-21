package com.bcp.challenge.domain.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateExchangeRequest {

    @NotNull(message = "La propiedad [moneda] no puede ser null")
    @Pattern(regexp="([A-Za-z]{3})",message = "La propiedad [moneda] debe tener un formato de 3 caracteres")
    private String moneda;
    @NotNull(message = "La propiedad [monto] no puede ser null")
    private Double monto;

}
