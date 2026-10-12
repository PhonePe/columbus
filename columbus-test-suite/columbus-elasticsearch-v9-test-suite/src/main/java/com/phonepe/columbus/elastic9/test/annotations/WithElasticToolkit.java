package com.phonepe.columbus.elastic9.test.annotations;

import com.phonepe.columbus.elastic9.test.extensions.ElasticToolkitExtension;
import com.phonepe.columbus.test.annotations.WithToolkit;
import com.phonepe.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(ElasticToolkitExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithElasticToolkit {

    ValueProps value() default @ValueProps;

    ValueProps.Key toolkit() default @ValueProps.Key;

    @WithToolkit.All
    @WithElasticToolkit
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface All {
    }
}
