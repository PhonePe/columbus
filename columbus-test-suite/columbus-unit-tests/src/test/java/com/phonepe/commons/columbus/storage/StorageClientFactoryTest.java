package com.phonepe.commons.columbus.storage;

import com.phonepe.commons.columbus.core.settings.RegistrySettings;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.storage.DummyClientFactory.DummyStorageProvider;
import com.phonepe.commons.columbus.storage.commands.VersionCommand;
import com.phonepe.commons.columbus.storage.settings.StorageSettings;
import com.phonepe.commons.columbus.test.annotations.WithToolkit;
import com.phonepe.commons.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.commons.columbus.test.storage.memory.MemoryStorageConfiguration;
import com.phonepe.commons.columbus.test.storage.memory.factory.MemoryStorageProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithToolkit.All
class StorageClientFactoryTest {

    private static final VersionCommand COMMAND = VersionCommand.builder().build();
    private static final MemoryStorageConfiguration configuration = MemoryStorageConfiguration.builder()
            .enabled(true)
            .name("memory-test")
            .build();

    @Test
    @DisplayName("create memory client using factory")
    void testCreateClient(final Toolkit toolkit) {
        final var settings = StorageSettings.defaults();

        final var factory = Storages.storageClientFactory(settings, toolkit);

        final var storage = factory.create(configuration);

        assertAll(
                "memory storage",
                () -> assertInstanceOf(MemoryStorage.class, storage,
                        "created instance must match the type of configuration"),
                () -> assertEquals(configuration.getName(), storage.name(), "name should match the configuration"),
                () -> assertEquals(configuration.getType(), storage.type(), "type should match the configuration"),
                () -> assertEquals(MemoryStorage.VERSION, COMMAND.execute(storage), "version should match"));
    }

    @Test
    @DisplayName("create client without registered visitors")
    void testCreateClientUnregistered(final Toolkit toolkit) {
        final var settings = StorageSettings.builder()
                .visitorOverrides(RegistrySettings.builder()
                        .defaultTypeFilter(type -> !MemoryStorageProvider.class.equals(type))
                        .build())
                .commandHandlerOverrides(RegistrySettings.defaults())
                .build();

        final var factory = Storages.storageClientFactory(settings, toolkit);

        assertThrows(
                IllegalArgumentException.class, () -> factory.create(configuration),
                "no visitor for memory configuration");
    }

    @Test
    @DisplayName("create memory client by name")
    void testCreateClientByName(final Toolkit toolkit) {
        final var settings = StorageSettings.builder()
                .visitorOverrides(RegistrySettings.builder()
                        .defaultTypeFilter(type -> !MemoryStorageProvider.class.equals(type))
                        .build())
                .commandHandlerOverrides(RegistrySettings.defaults())
                .nameOverride(configuration.getName(), new MemoryStorageProvider<>())
                .nameOverride("dummy", new DummyStorageProvider<>())
                .build();

        final var factory = Storages.storageClientFactory(settings, toolkit);

        final var storage = factory.create(configuration);

        assertAll(
                "memory storage",
                () -> assertInstanceOf(MemoryStorage.class, storage,
                        "created instance must match the type of configuration"),
                () -> assertEquals(configuration.getName(), storage.name(), "name should match the configuration"),
                () -> assertEquals(configuration.getType(), storage.type(), "type should match the configuration"),
                () -> assertEquals(MemoryStorage.VERSION, COMMAND.execute(storage), "version should match"));
    }
}
