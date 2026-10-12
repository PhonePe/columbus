package com.phonepe.columbus.elastic9.storage.configuration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.elastic9.common.Constants;
import com.phonepe.columbus.elastic9.storage.configuration.auth.AuthConfiguration;
import com.phonepe.columbus.storage.StorageConfiguration;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * A set of parameters to configure Elastic clients.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(ElasticConfiguration.TYPE)
@SuppressWarnings("java:S2160")
public class ElasticConfiguration extends StorageConfiguration {

    public static final String TYPE = Constants.NAME;

    @Valid
    @NotNull
    private final HostConfiguration hosts;

    @Valid
    private final AuthConfiguration auth;

    @Valid
    private final RequestConfiguration requestConfig;

    @Valid
    private final IndexConfiguration indices;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
