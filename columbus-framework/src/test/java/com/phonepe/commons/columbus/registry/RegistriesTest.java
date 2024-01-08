package com.phonepe.commons.columbus.registry;

import com.phonepe.commons.columbus.internal.registry.StoredRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistriesTest {

    @Test
    @DisplayName("empty registry always returns no values")
    void testEmptyRegistry() {
        final Registry<Void, String> registry = Registries.empty();
        assertTrue(registry.get(null).isEmpty());
        assertRegistryIsEmpty(registry);
    }

    @Test
    @DisplayName("creates empty registry if no entries are present in the map")
    void testValuesEmptyWhenEntriesEmpty() {
        final var registry = Registries.withValues(Map.of());
        assertRegistriesAreEmpty(registry);
    }

    @Test
    @DisplayName("creates registry with provided values")
    void testValueRegistry() {
        final var registry = Registries.withValues(Map.of("key", "value"));
        assertAll(
                "stored registry",
                () -> assertInstanceOf(StoredRegistry.class, registry, "creates a stored registry with values"),
                () -> assertEquals("value", registry.get("key").orElse(null), "must retain provided values"));
    }

    @Test
    @DisplayName("creates empty registry if delegate for cache is empty")
    void testCacheEmptyWhenDelegateEmpty() {
        final var delegate = Registries.empty();
        final var registry = Registries.cached(delegate);
        assertRegistriesAreEmpty(delegate, registry);
    }

    @Test
    @DisplayName("creates empty registry if delegate for transform is empty")
    void testTransformEmptyWhenDelegateEmpty() {
        final var delegate = Registries.<String, Object>empty();
        final var registry = Registries.transforming(Object::toString, delegate, Object::toString);
        assertRegistriesAreEmpty(delegate, registry);
    }

    @Test
    @DisplayName("creates empty registry if delegate for key transform is empty")
    void testKeyTransformEmptyWhenDelegateEmpty() {
        final var delegate = Registries.<String, Object>empty();
        final var registry = Registries.transformingKey(Object::toString, delegate);
        assertRegistriesAreEmpty(delegate, registry);
    }

    @Test
    @DisplayName("creates empty registry if delegate for value transform is empty")
    void testValueTransformEmptyWhenDelegateEmpty() {
        final var delegate = Registries.<String, Object>empty();
        final var registry = Registries.transformingValue(delegate, Object::toString);
        assertRegistriesAreEmpty(delegate, registry);
    }

    @Test
    @DisplayName("creates empty registry if delegate for monitoring is empty")
    void testMonitorEmptyWhenDelegateEmpty() {
        final var delegate = Registries.<String, Object>empty();
        final var registry = Registries.monitorIfNotEmpty(delegate, null);
        assertRegistriesAreEmpty(delegate, registry);
    }

    @Test
    @DisplayName("chain registry does not add empty registries")
    void testChainWithEmptyRegistries() {
        final var delegate = Registries.empty();
        final var registry = Registries.chaining(delegate)
                .build();
        assertRegistryIsEmpty(delegate);
        assertTrue(registry.registries().isEmpty(), "chain should have no registries");
    }

    @Test
    @DisplayName("chain registry does not add null registries")
    void testChainWithNullRegistries() {
        final var registry = Registries.chaining()
                .add(null)
                .build();
        assertTrue(registry.registries().isEmpty(), "chain should have no registries");
    }

    @Test
    @DisplayName("chain registry flattens inner chained registries")
    void testChainFlattensChainedRegistries() {
        final var inner = Registries.chaining()
                .add(Registries.stored())
                .add(Registries.empty())
                .add(Registries.stored())
                .build();

        final var registry = Registries.chaining(inner)
                .add(null)
                .add(Registries.stored())
                .build();

        assertEquals(2, inner.registries().size(), "chain should only have non empty registries");
        assertEquals(3, registry.registries().size(), "chain should flatten inner chain registries");
        assertAll(
                "chain has no chain",
                registry.registries().stream()
                        .map(mutable -> () ->
                                assertInstanceOf(MutableRegistry.class, mutable, "inner registry is mutable")));
    }

    @Test
    @DisplayName("creates empty registry if delegate for hierarchical key is empty")
    void testHierarchicalKeyEmptyWhenDelegateEmpty() {
        final var delegate = Registries.<String, Object>empty();
        final var registry = Registries.hierarchicalKey(delegate, object -> Optional.empty());
        assertRegistriesAreEmpty(delegate, registry);
    }

    @Test
    @DisplayName("class hierarchy identifies parent class types")
    void testClassHierarchyRegistry() {
        final var classRegistry = Registries.stored(Map.of(
                AbstractCollection.class, "collection",
                Object.class, "object"
        ));
        final var registry = Registries.usingClassHierarchy(classRegistry, null);
        final var list = List.of();
        final var string = "sample";

        assertAll(
                "registry entry",
                () -> assertEquals("collection", registry.get(list).orElse(null), "list is resolved as a collection"),
                () -> assertEquals("object", registry.get(string).orElse(null), "string is resolved as an object"));
    }

    private static void assertRegistriesAreEmpty(final Registry<?, ?>... registries) {
        assertAll(
                "registries should be empty",
                Arrays.stream(registries).map(registry -> () -> assertRegistryIsEmpty(registry)));
    }

    private static void assertRegistryIsEmpty(final Registry<?, ?> registry) {
        assertTrue(Registries.isEmpty(registry), "registry should be empty");
    }
}
