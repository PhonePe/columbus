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

import com.phonepe.columbus.storage.StorageConfiguration;
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
