package com.phonepe.platform.columbus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phonepe.platform.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.platform.columbus.storage.StorageConfiguration;
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
