package com.phonepe.platform.columbus.test.annotations;

import com.phonepe.platform.columbus.test.extensions.UniqueStringProviderExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(UniqueStringProviderExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithUniqueString {

    String format() default UniqueStringProviderExtension.DEFAULT_FORMAT;

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Inject {}
}
