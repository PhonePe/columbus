package com.phonepe.columbus.elastic9.storage.configuration;

import com.phonepe.columbus.common.http.Endpoint;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Jacksonized
public class HostConfiguration {

    @Valid
    @NotEmpty
    @Singular(ignoreNullCollections = true)
    private final List<@Valid @NotNull Endpoint> endpoints;

    private final String pathPrefix;

    private final boolean compressionEnabled;

    private final boolean yellowUnhealthy;
}
