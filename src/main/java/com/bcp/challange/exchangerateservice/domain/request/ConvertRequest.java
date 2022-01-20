package com.bcp.challange.exchangerateservice.domain.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ConvertRequest {

    @NotNull(message = "La propiedad [monto] no puede ser null")
    private Double monto;
    @NotNull(message = "La propiedad [monedaOrigen] no puede ser null")
    @Pattern(regexp="([A-Za-z]{3})",message = "La propiedad [monedaOrigen] debe tener un formato de 3 caracteres")
    private String monedaOrigen;
    @NotNull(message = "La propiedad [monedaDestino] no puede ser null")
    @Pattern(regexp="([A-Za-z]{3})",message = "La propiedad [monedaDestino] debe tener un formato de 3 caracteres")
    private String monedaDestino;

}
