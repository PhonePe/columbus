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
package com.phonepe.columbus.elastic9.storage.configuration;

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
