package com.phonepe.commons.columbus.registry.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to capture the details of the key against which an instance of the annotated class will be stored.
 * <p>
 * The key is expected to either be a string, represented by {@link #name() name}, or a class, represented by
 * {@link #type() type}.
 * <p>
 * The value of {@link #priority() priority} is used to resolve conflicts. The annotation with the highest value will
 * be chosen.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RegistryKey.List.class)
public @interface RegistryKey {

    String name() default "";

    Class<?> type() default Void.class;

    int priority() default 0;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        RegistryKey[] value();
    }
}
