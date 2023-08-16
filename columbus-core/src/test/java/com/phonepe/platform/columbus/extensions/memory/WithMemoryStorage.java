package com.phonepe.platform.columbus.extensions.memory;

import com.phonepe.platform.columbus.extensions.toolkit.WithToolkit;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(MemoryStorageExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithMemoryStorage {

    ValueProps value() default @ValueProps;

    ValueProps.Key toolkit() default @ValueProps.Key;

    @WithMemoryStorage(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }

    @WithToolkit.All
    @WithMemoryStorage
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface All {
    }
}
