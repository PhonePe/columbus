package com.phonepe.columbus.common.initializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Initialize a value lazily. A precondition can be provided to check if the value can be initialized.
 *
 * @param <T> the type of the value.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LazyInit<T> implements Initializer<T> {

    private static final String DEFAULT_NAME = "Object";

    @NonNull
    private final String name;
    @NonNull
    private final BooleanSupplier precondition;
    @NonNull
    private final Supplier<T> factory;

    @NonNull
    private final LateInit<T> value;

    public LazyInit(final Supplier<T> factory) {
        this(DEFAULT_NAME, factory);
    }

    public LazyInit(final String name, final Supplier<T> factory) {
        this(name, () -> true, factory);
    }

    public LazyInit(final BooleanSupplier precondition, final Supplier<T> factory) {
        this(DEFAULT_NAME, precondition, factory);
    }

    public LazyInit(final String name, final BooleanSupplier precondition, final Supplier<T> factory) {
        this(name, precondition, factory, new LateInit<>(name));
    }

    /**
     * Get the value, initializing it if necessary.
     *
     * @return the value held by this.
     * @throws IllegalStateException if the value could not be initialized due to the precondition.
     */
    @Override
    public T get() {
        if (value.isPresent()) {
            return value.get();
        }
        if (!precondition.getAsBoolean()) {
            throw new IllegalStateException("%s cannot be initialized".formatted(name));
        }
        value.initializeWith(factory);
        return value.get();
    }

    /**
     * Check if the value is already initialized or can be initialized.
     *
     * @return true if the value can be initialized, false otherwise.
     */
    @Override
    public boolean isPresent() {
        return value.isPresent() || precondition.getAsBoolean();
    }
}
