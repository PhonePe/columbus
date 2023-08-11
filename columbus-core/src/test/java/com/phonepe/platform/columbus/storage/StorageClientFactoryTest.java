package com.phonepe.platform.columbus.storage;

import com.phonepe.platform.columbus.core.settings.RegistrySettings;
import com.phonepe.platform.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.platform.columbus.storage.DummyClientFactory.DummyStorageProvider;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.storage.memory.MemoryStorageConfiguration;
import com.phonepe.platform.columbus.storage.memory.factory.MemoryStorageProvider;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        final var factory = Storages.storageClientFactory(settings, toolkit);

        final var command = VersionCommand.builder().build();

        final var storage = factory.create(configuration);

        assertAll(
                "memory storage",
                () -> assertInstanceOf(MemoryStorage.class, storage,
                        "created instance must match the type of configuration"),
                () -> assertEquals(configuration.getName(), storage.name(), "name should match the configuration"),
                () -> assertEquals(configuration.getType(), storage.type(), "type should match the configuration"),
                () -> assertEquals(MemoryStorage.VERSION, command.execute(storage), "version should match"));
    }

    @Test
    @DisplayName("create client without registered visitors")
    void testCreateClientUnregistered() {
        final var toolkit = ToolkitHelpers.defaultInstance();
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
    void testCreateClientByName() {
        final var toolkit = ToolkitHelpers.defaultInstance();
        final var settings = StorageSettings.builder()
                .visitorOverrides(RegistrySettings.builder()
                        .defaultTypeFilter(type -> !MemoryStorageProvider.class.equals(type))
                        .build())
                .commandHandlerOverrides(RegistrySettings.defaults())
                .nameOverride(configuration.getName(), new MemoryStorageProvider<>())
                .nameOverride("dummy", new DummyStorageProvider<>())
                .build();

        final var factory = Storages.storageClientFactory(settings, toolkit);

        final var command = VersionCommand.builder().build();

        final var storage = factory.create(configuration);

        assertAll(
                "memory storage",
                () -> assertInstanceOf(MemoryStorage.class, storage,
                        "created instance must match the type of configuration"),
                () -> assertEquals(configuration.getName(), storage.name(), "name should match the configuration"),
                () -> assertEquals(configuration.getType(), storage.type(), "type should match the configuration"),
                () -> assertEquals(MemoryStorage.VERSION, command.execute(storage), "version should match"));
    }
}
