package com.phonepe.commons.columbus.common.http;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class Header {

    @NotBlank
    private final String name;

    private final String value;
}
