package com.bcp.challenge.domain.response;

import com.bcp.challenge.domain.response.error.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BasicResponse {

    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorResponse> errors;

}
