package com.phonepe.commons.columbus.test.annotations;

import com.phonepe.commons.columbus.test.extensions.DropwizardStorageFactoryExtension;
import com.phonepe.commons.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(DropwizardStorageFactoryExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithDropwizardStorageFactory {

    ValueProps value() default @ValueProps;

    ValueProps.Key toolkit() default @ValueProps.Key;

    ValueProps.Key environment() default @ValueProps.Key;

    @WithDropwizardStorageFactory(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }

    @WithToolkit.All
    @WithMockDropwizardEnvironment
    @WithDropwizardStorageFactory
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface All {
    }
}
