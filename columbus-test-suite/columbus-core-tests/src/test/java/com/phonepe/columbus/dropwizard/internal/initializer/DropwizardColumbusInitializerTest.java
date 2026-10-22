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
package com.phonepe.columbus.dropwizard.internal.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.columbus.core.settings.ColumbusSettings;
import com.phonepe.columbus.storage.StorageConfiguration;
import com.phonepe.columbus.test.annotations.WithMapper;
import com.phonepe.columbus.test.annotations.WithMockDropwizardEnvironment;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.columbus.test.storage.memory.MemoryStorageConfiguration;
import io.dropwizard.setup.Environment;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WithMapper
@WithMockDropwizardEnvironment
class DropwizardColumbusInitializerTest {

    private static final ColumbusSettings settings = ColumbusSettings.defaults();
    private static final MemoryStorageConfiguration storageConfiguration = MemoryStorageConfiguration.builder()
            .enabled(true)
            .name("memory-test")
            .build();

    @Test
    @DisplayName("validate initialization step by step")
    void testInitializeInSteps(final ObjectMapper mapper, final Environment environment) {
        final var configuration = ColumbusTestConfiguration.builder()
                .storage(storageConfiguration)
                .build();

        final var initializer = new DropwizardColumbusInitializer();
        assertFalse(initializer.canInitializeStorageClientFactory(), "storage factory cannot be initialized");
        initializer.use(settings);
        initializer.use(mapper);
        initializer.use(configuration);
        assertFalse(initializer.canInitializeStorageClientFactory(), "storage factory cannot be initialized");
        initializer.use(environment);
        assertTrue(initializer.canInitializeStorageClientFactory(), "storage factory can be initialized");

        final var columbus = initializer.get();

        assertAll(
                "Columbus",
                () -> assertNotNull(columbus, "must return a valid Columbus"),
                () -> assertNotNull(columbus.toolkit(), "must have a valid toolkit"),
                () -> assertInstanceOf(
                        MemoryStorage.class, columbus.storage(storageConfiguration.getName()),
                        "must have a valid storage"),
                () -> assertEquals(1, columbus.storages().size(), "must have one storage"));
    }

    @Data
    @Builder
    public static class ColumbusTestConfiguration implements ColumbusConfiguration {

        @Builder.Default
        private final boolean enabled = true;

        @Singular(ignoreNullCollections = true)
        private final List<StorageConfiguration> storages;
    }
}
