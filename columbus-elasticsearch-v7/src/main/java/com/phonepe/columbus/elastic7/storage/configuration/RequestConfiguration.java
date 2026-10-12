package com.phonepe.columbus.elastic7.storage.configuration;

import com.phonepe.columbus.common.http.Endpoint;
import com.phonepe.columbus.common.http.Header;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Jacksonized
public class RequestConfiguration {

    @Valid
    @Singular(ignoreNullCollections = true)
    private final List<@Valid @NotNull Header> defaultHeaders;

    @Min(0)
    private final int maxTotalConnections;
    @Min(0)
    private final int maxConnectionsPerRoute;
    @Min(0)
    private final int connectionRequestTimeout;
    @Min(0)
    private final int connectTimeout;
    @Min(0)
    private final int socketTimeout;

    private final boolean systemPropertiesEnabled;

    @Valid
    private final Endpoint proxy;

    private final String userAgent;
}
