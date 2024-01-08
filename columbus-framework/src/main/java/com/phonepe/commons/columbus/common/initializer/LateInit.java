package com.phonepe.commons.columbus.common.initializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A thread-safe wrapper to allow initializing an immutable field later, but only once.
 * <p>
 * Specify a name for better error messages.
 *
 * @param <T> the type of the value.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class LateInit<T> implements Initializer<T> {

    private static final String DEFAULT_NAME = "object";

    @NonNull
    private final String name;
    @NonNull
    private final AtomicReference<Holder<T>> reference;

    public LateInit() {
        this(DEFAULT_NAME);
    }

    public LateInit(final String name) {
        this(name, new AtomicReference<>());
    }

    public LateInit(final T value) {
        this(DEFAULT_NAME, value);
    }

    public LateInit(final String name, final T value) {
        this(name, new AtomicReference<>(new Holder<>(value)));
    }

    /**
     * Get the initialized value.
     *
     * @return the containing value.
     * @throws IllegalStateException if this was not initialized.
     */
    @Override
    public T get() {
        final var value = reference.get();
        if (value == null) {
            throw new IllegalStateException("%s is not initialized".formatted(name));
        }
        return value.value();
    }

    /**
     * Check if this is initialized.
     *
     * @return true if this is initialized with a value, false otherwise.
     */
    @Override
    public boolean isPresent() {
        return reference.get() != null;
    }

    /**
     * Initialize the holder with the given value, if not already initialized.
     *
     * @param value the value to be set.
     * @return true if the initialization succeeds, false otherwise. Initialization fails if this is already
     * initialized.
     */
    public boolean initialize(final T value) {
        return reference.compareAndSet(null, new Holder<>(value));
    }

    /**
     * Create and set a new value if this is empty.
     *
     * @param supplier a function to create the new value.
     * @return true if a new value is set, false if there is already a value.
     */
    public synchronized boolean initializeWith(final Supplier<T> supplier) {
        if (!isPresent()) {
            return initialize(supplier.get());
        }
        return false;
    }

    /**
     * Initialize the holder with the given value.
     *
     * @param value the value to be set.
     * @throws IllegalStateException if the holder is already initialized.
     */
    public void set(final T value) {
        if (!initialize(value)) {
            throw new IllegalStateException("%s is already initialized".formatted(name));
        }
    }

    private record Holder<T>(T value) {}
}
