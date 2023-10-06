package com.phonepe.platform.columbus.test.annotations;

import com.phonepe.platform.columbus.test.extensions.MemoryStorageExtension;
import com.phonepe.platform.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(MemoryStorageExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithMemoryStorage {

    ValueProps value() default @ValueProps;

    ValueProps.Key storageFactory() default @ValueProps.Key;

    @WithMemoryStorage(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }

    @WithStorageFactory.All
    @WithMemoryStorage
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface All {
    }
}
