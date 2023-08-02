package com.phonepe.platform.columbus.common.initializer;

/**
 * An object that represents a value that can be initialized exactly once.
 *
 * @param <T> the type of the value this object holds.
 */
public interface Initializer<T> {

    /**
     * Get the value held by this object.
     * <p>
     * Use {@link #isPresent()} before calling this method. If the result is false, this method might throw an
     * exception.
     *
     * @return the containing value.
     * @throws IllegalStateException if this is not initialized.
     */
    T get();

    /**
     * Check if this object has a value.
     *
     * @return true if a value could be returned by {@link #get()}, false otherwise.
     */
    boolean isPresent();
}
