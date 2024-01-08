package com.phonepe.commons.columbus.test.annotations;

import com.phonepe.commons.columbus.test.extensions.ElasticClientExtension;
import com.phonepe.commons.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(ElasticClientExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithElasticClient {

    ValueProps value() default @ValueProps;

    ValueProps.Key storageFactory() default @ValueProps.Key;

    ValueProps.Key container() default @ValueProps.Key;

    @WithElasticClient(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }

    @WithDropwizardStorageFactory.All
    @WithElasticContainer.Root
    @WithElasticClient
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface All {
    }
}
