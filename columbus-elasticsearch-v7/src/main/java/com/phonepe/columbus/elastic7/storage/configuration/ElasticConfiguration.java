/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic7.storage.configuration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.elastic7.common.Constants;
import com.phonepe.columbus.elastic7.storage.configuration.auth.AuthConfiguration;
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
