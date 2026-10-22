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
package com.phonepe.columbus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phonepe.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.columbus.storage.StorageConfiguration;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * A concrete implementation of {@link ColumbusConfiguration} suited to Dropwizard bundles with deserialization and
 * validations handled.
 */
@Data
@Builder
@Jacksonized
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
public class ColumbusBundleConfiguration implements ColumbusConfiguration {

    @Builder.Default
    private final boolean enabled = true;

    @Valid
    @Singular(ignoreNullCollections = true)
    private final List<@NonNull StorageConfiguration> storages;

    /**
     * Only to validate that the names of the active storages are unique.
     *
     * @return the list of names of enabled storages.
     */
    @Valid
    @JsonIgnore
    @UniqueElements
    private List<@NotNull String> getStorageNames() {
        return storages.stream()
                .filter(StorageConfiguration::isEnabled)
                .map(StorageConfiguration::getName)
                .toList();
    }
}
