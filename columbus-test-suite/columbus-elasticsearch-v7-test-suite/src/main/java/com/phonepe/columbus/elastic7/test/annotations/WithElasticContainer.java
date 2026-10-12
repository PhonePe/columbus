package com.phonepe.columbus.elastic7.test.annotations;

import com.phonepe.columbus.elastic7.test.extensions.ElasticContainerExtension;
import com.phonepe.columbus.test.value.ValueProps;
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

    String imageEnvOverride() default ElasticContainerExtension.ELASTIC_SEARCH_DOCKER_IMAGE_ENV;

    String version() default ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_VERSION;

    String password() default "";

    @WithElasticContainer(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }
}
