package com.phonepe.columbus.internal.core.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.columbus.core.settings.ColumbusSettings;
import com.phonepe.columbus.storage.StorageConfiguration;
import com.phonepe.columbus.test.annotations.WithMapper;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.columbus.test.storage.memory.MemoryStorageConfiguration;
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
class ColumbusInitializerTest {

    private static final ColumbusSettings settings = ColumbusSettings.defaults();
    private static final MemoryStorageConfiguration storageConfiguration = MemoryStorageConfiguration.builder()
            .enabled(true)
            .name("memory-test")
            .build();

    @Test
    @DisplayName("initialize")
    void testInitialize(final ObjectMapper mapper) {
        final var configuration = ColumbusTestConfiguration.builder()
                .storage(storageConfiguration)
                .build();

        final var initializer = new DefaultColumbusInitializer();
        initializer.use(settings);
        initializer.use(mapper);
        initializer.use(configuration);

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

    @Test
    @DisplayName("initialize disabled")
    void testInitializeDisabled(final ObjectMapper mapper) {
        final var configuration = ColumbusTestConfiguration.builder()
                .enabled(false)
                .storage(storageConfiguration)
                .build();

        final var initializer = new DefaultColumbusInitializer();
        initializer.use(settings);
        initializer.use(mapper);
        initializer.use(configuration);

        final var columbus = initializer.get();

        assertAll(
                "Columbus",
                () -> assertNotNull(columbus, "must return a valid Columbus"),
                () -> assertNotNull(columbus.toolkit(), "must have a valid toolkit"),
                () -> assertTrue(columbus.storages().isEmpty(), "storages must not be initialized"));
    }

    @Test
    @DisplayName("validate initialization step by step")
    void testInitializeInSteps(final ObjectMapper mapper) {
        final var configuration = ColumbusTestConfiguration.builder()
                .storage(storageConfiguration)
                .build();

        final var initializer = new DefaultColumbusInitializer();
        assertFalse(initializer.getToolkitInitializer().isPresent(), "toolkit initializer cannot be initialized");
        assertFalse(initializer.getToolkit().isPresent(), "toolkit cannot be initialized");
        assertFalse(initializer.getStorageClientFactory().isPresent(), "storage factory cannot be initialized");
        assertFalse(initializer.getStorages().isPresent(), "storages cannot be initialized");
        assertFalse(initializer.getColumbus().isPresent(), "Columbus cannot be initialized");
        initializer.use(settings);
        assertFalse(initializer.getColumbus().isPresent(), "Columbus cannot be initialized");
        assertTrue(initializer.getToolkitInitializer().isPresent(), "toolkit initializer can be initialized");
        assertFalse(initializer.getToolkit().isPresent(), "toolkit cannot be initialized");
        assertFalse(initializer.getStorageClientFactory().isPresent(), "storage factory cannot be initialized");
        assertFalse(initializer.getStorages().isPresent(), "storages cannot be initialized");
        initializer.use(mapper);
        assertFalse(initializer.getColumbus().isPresent(), "Columbus cannot be initialized");
        assertTrue(initializer.getToolkit().isPresent(), "toolkit can be initialized");
        final var toolkit = initializer.toolkit();
        assertTrue(initializer.getStorageClientFactory().isPresent(), "storage factory can be initialized");
        assertFalse(initializer.getStorages().isPresent(), "storages cannot be initialized");
        initializer.use(configuration);
        assertTrue(initializer.getStorages().isPresent(), "storages can be initialized");
        assertTrue(initializer.getColumbus().isPresent(), "Columbus can be initialized");

        final var columbus = initializer.get();

        assertAll(
                "Columbus",
                () -> assertNotNull(columbus, "must return a valid Columbus"),
                () -> assertNotNull(columbus.toolkit(), "must have a valid toolkit"),
                () -> assertEquals(toolkit, columbus.toolkit(), "must match toolkit from initializer"),
                () -> assertInstanceOf(MemoryStorage.class, columbus.storage(storageConfiguration.getName()),
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
