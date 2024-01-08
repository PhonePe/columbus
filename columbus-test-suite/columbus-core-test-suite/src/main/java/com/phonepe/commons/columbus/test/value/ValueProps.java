package com.phonepe.commons.columbus.test.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface ValueProps {

    Key key() default @Key;

    boolean useRootContext() default false;

    boolean cleanUp() default true;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @interface Key {

        Class<?> keyClass() default Void.class;

        String keyName() default "";
    }
}
