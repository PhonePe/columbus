package com.phonepe.commons.columbus.test.annotations;

import com.phonepe.commons.columbus.test.extensions.UniqueIdProviderExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(UniqueIdProviderExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithUniqueTestId {

    String format() default UniqueIdProviderExtension.DEFAULT_FORMAT;
}
