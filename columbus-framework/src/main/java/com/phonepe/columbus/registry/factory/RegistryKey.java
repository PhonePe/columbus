/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.registry.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to capture the details of the key against which an instance of the annotated class will be stored.
 * <p>
 * The key is expected to either be a string, represented by {@link #name() name}, or a class, represented by
 * {@link #type() type}.
 * <p>
 * The value of {@link #priority() priority} is used to resolve conflicts. The annotation with the highest value will
 * be chosen.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RegistryKey.List.class)
public @interface RegistryKey {

    String name() default "";

    Class<?> type() default Void.class;

    int priority() default 0;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        RegistryKey[] value();
    }
}
