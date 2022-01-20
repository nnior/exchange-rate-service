package com.bcp.challenge.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

    @Id
    private Long id;
    private String baseAsset;
    private String quoteAsset;
    private BigDecimal rate;

}
