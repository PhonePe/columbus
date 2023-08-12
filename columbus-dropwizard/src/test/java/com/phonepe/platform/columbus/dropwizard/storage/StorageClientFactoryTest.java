package com.phonepe.platform.columbus.dropwizard.storage;

import com.phonepe.platform.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorageConfiguration;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import io.dropwizard.setup.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

class StorageClientFactoryTest {

    private static final MemoryStorageConfiguration configuration = MemoryStorageConfiguration.builder()
            .enabled(true)
            .name("memory-test")
            .build();

    @Test
    @DisplayName("create memory client using factory")
    void testCreateClient() {
        final var toolkit = ToolkitHelpers.defaultInstance();
        final var settings = StorageSettings.defaults();

        final var environment = mock(Environment.class);

        final var factory = DropwizardStorages.storageClientFactory(settings, toolkit, environment);

        final var command = VersionCommand.builder().build();

        final var storage = factory.create(configuration);

        assertAll(
                "memory storage",
                () -> assertInstanceOf(
                        MemoryStorage.class, storage,
                        "created instance must match the type of configuration"),
                () -> assertEquals(configuration.getName(), storage.name(), "name should match the configuration"),
                () -> assertEquals(configuration.getType(), storage.type(), "type should match the configuration"),
                () -> assertEquals(environment, ((MemoryStorage) storage).getEnvironment(),
                        "environment must be present"),
                () -> assertEquals(MemoryStorage.VERSION, command.execute(storage), "version should match"));
    }
}
