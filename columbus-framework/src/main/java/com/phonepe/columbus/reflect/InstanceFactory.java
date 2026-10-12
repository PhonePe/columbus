package com.phonepe.columbus.reflect;

/**
 * A factory to initialize an instance from its {@link Class}.
 */
public interface InstanceFactory {

    /**
     * Creates an instance of the given class.
     *
     * @param type the class to create an instance of.
     * @param <T>  the actual type of the instance.
     * @return the created instance.
     * @throws IllegalArgumentException if the type cannot be instantiated.
     * @throws NullPointerException     if the type is null.
     */
    <T> T getInstance(Class<T> type);
}
