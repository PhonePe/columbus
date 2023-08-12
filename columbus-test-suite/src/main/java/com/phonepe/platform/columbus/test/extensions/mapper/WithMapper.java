package com.phonepe.platform.columbus.test.extensions.mapper;

import com.phonepe.platform.columbus.test.extensions.ValueProps;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(MapperExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface WithMapper {

    ValueProps value() default @ValueProps;

    @WithMapper(@ValueProps(useRootContext = true, cleanUp = false))
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @interface Root {
    }
}
