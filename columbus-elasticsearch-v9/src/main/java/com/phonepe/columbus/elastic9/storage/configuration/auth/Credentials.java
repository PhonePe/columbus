package com.phonepe.columbus.elastic9.storage.configuration.auth;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class Credentials {

    @NotBlank
    private final String username;

    @NotBlank
    private final String password;
}
