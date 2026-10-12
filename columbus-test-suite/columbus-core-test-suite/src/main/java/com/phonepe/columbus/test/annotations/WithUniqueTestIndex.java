package com.phonepe.columbus.test.annotations;

import com.phonepe.columbus.test.extensions.UniqueIndexProviderExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(UniqueIndexProviderExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithUniqueTestIndex {

    String format() default UniqueIndexProviderExtension.DEFAULT_FORMAT;
}
