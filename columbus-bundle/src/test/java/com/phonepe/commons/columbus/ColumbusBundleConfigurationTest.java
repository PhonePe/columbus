package com.phonepe.commons.columbus;

import com.phonepe.commons.columbus.storage.StorageConfiguration;
import io.dropwizard.jersey.validation.Validators;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColumbusBundleConfigurationTest {

    private static final Validator validator = Validators.newValidator();

    @Test
    @DisplayName("valid configuration")
    void testValidConfiguration() {
        final var configuration = ColumbusBundleConfiguration.builder()
                .storage(DummyStorageConfiguration.of(true, "dummy1"))
                .storage(DummyStorageConfiguration.of(true, "dummy2"))
                .storage(DummyStorageConfiguration.of(false, "dummy1"))
                .build();

        final var violations = validator.validate(configuration);

        assertTrue(violations.isEmpty(), "must not have any violations");
    }

    @Test
    @DisplayName("storage names are not unique")
    void testNonUniqueNames() {
        final var configuration = ColumbusBundleConfiguration.builder()
                .storage(DummyStorageConfiguration.of(true, "dummy1"))
                .storage(DummyStorageConfiguration.of(true, "dummy2"))
                .storage(DummyStorageConfiguration.of(true, "dummy1"))
                .build();

        final var violations = validator.validate(configuration);

        assertEquals(1, violations.size(), "uniqueness constraint must be violated");
    }

    @Data
    @SuperBuilder
    public static class DummyStorageConfiguration extends StorageConfiguration {

        public static DummyStorageConfiguration of(final boolean enabled, final String name) {
            return builder()
                    .enabled(enabled)
                    .name(name)
                    .build();
        }

        @Override
        public @NonNull String getType() {
            return "DUMMY";
        }
    }
}
