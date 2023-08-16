package com.phonepe.platform.columbus.common.http;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class Endpoint {

    @NotBlank
    private final String host;

    @Min(-1)
    @Max(65535)
    @Builder.Default
    private final int port = -1;

    private final String scheme;
}
