package com.phonepe.commons.columbus.search.algorithms.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {

    private final String text;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean tail = false;
}
