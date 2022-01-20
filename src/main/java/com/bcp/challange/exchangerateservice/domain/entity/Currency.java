package com.bcp.challange.exchangerateservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @Id
    private Long id;
    private String symbol;
    private String description;

}
