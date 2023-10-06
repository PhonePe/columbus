package com.phonepe.platform.columbus.test.annotations;

import com.phonepe.platform.columbus.test.extensions.ElasticContainerExtension;
import com.phonepe.platform.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(ElasticContainerExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithElasticContainer {

    ValueProps value() default @ValueProps;

    String image() default ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_IMAGE;

    String version() default ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_VERSION;

    String password() default "";

    @WithElasticContainer(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }
}
