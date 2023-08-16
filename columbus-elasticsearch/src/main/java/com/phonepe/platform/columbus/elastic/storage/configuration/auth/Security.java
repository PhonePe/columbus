package com.phonepe.platform.columbus.elastic.storage.configuration.auth;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class Security {

    @NotBlank
    private final String trustStorePath;

    @NotBlank
    private final String keyStoreType;

    @NotBlank
    private final String keyStorePassword;
}
