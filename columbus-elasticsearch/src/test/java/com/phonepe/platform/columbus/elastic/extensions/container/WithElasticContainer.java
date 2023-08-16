package com.phonepe.platform.columbus.elastic.extensions.container;

import com.phonepe.platform.columbus.elastic.extensions.TestConstants;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(ElasticContainerExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithElasticContainer {

    ValueProps value() default @ValueProps;

    String image() default TestConstants.ELASTIC_SEARCH_IMAGE;

    String version() default TestConstants.ELASTIC_SEARCH_VERSION;

    String password() default "";

    @WithElasticContainer(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }
}
