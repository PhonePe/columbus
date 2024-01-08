package com.phonepe.commons.columbus.elastic.storage.configuration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.elastic.common.Constants;
import com.phonepe.commons.columbus.elastic.storage.configuration.auth.AuthConfiguration;
import com.phonepe.commons.columbus.storage.StorageConfiguration;
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
