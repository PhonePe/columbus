package com.phonepe.commons.columbus.elastic9.storage.configuration.auth;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;

@Data
@Builder
@Jacksonized
public class AuthConfiguration {

    @Valid
    private final Credentials credentials;

    @Valid
    private final Security tls;
}
