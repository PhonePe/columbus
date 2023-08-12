package com.phonepe.platform.columbus.dropwizard.internal.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.platform.columbus.core.settings.ColumbusSettings;
import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorageConfiguration;
import com.phonepe.platform.columbus.storage.StorageConfiguration;
import io.dropwizard.setup.Environment;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DropwizardInitializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ColumbusSettings settings = ColumbusSettings.defaults();
    private static final MemoryStorageConfiguration storageConfiguration = MemoryStorageConfiguration.builder()
            .enabled(true)
            .name("memory-test")
            .build();
    private static final Environment environment = mock(Environment.class);

    @Test
    @DisplayName("initialize")
    void testInitialize() {
        final var configuration = ColumbusTestConfiguration.builder()
                .storage(storageConfiguration)
                .build();

        final var initializer = new DropwizardColumbusInitializer();
        initializer.use(settings);
        initializer.use(mapper);
        initializer.use(configuration);
        initializer.use(environment);

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
