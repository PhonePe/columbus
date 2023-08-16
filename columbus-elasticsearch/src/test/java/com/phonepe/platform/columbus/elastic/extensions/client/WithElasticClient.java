package com.phonepe.platform.columbus.elastic.extensions.client;

import com.phonepe.platform.columbus.elastic.extensions.container.WithElasticContainer;
import com.phonepe.platform.columbus.elastic.extensions.toolkit.WithToolkit;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import com.phonepe.platform.columbus.test.extensions.ValueProps.Key;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(ElasticClientExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithElasticClient {

    ValueProps value() default @ValueProps;

    Key toolkit() default @Key;

    Key container() default @Key;

    @WithElasticClient(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }

    @WithToolkit.All
    @WithElasticContainer.Root
    @WithElasticClient
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface All {
    }
}
