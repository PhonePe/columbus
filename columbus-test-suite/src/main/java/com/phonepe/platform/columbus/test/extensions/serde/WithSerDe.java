package com.phonepe.platform.columbus.test.extensions.serde;

import com.phonepe.platform.columbus.test.extensions.ValueProps;
import com.phonepe.platform.columbus.test.extensions.mapper.WithMapper;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(SerDeExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithSerDe {

    ValueProps value() default @ValueProps;

    ValueProps.Key mapper() default @ValueProps.Key;

    @WithSerDe(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }

    @WithMapper
    @WithSerDe
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface All {
    }
}
