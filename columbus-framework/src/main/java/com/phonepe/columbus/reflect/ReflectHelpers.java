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
package com.phonepe.columbus.reflect;

import com.phonepe.columbus.internal.reflect.ConstructorInstanceFactory;
import com.phonepe.columbus.internal.reflect.SimpleReflectFactory;
import com.phonepe.columbus.internal.reflect.SimpleReflectionInitializer;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create different types of instances required to set up {@link Reflect}.
 */
@UtilityClass
public class ReflectHelpers {

    public ConstructorInstanceFactory instanceFactory() {
        return new ConstructorInstanceFactory();
    }

    public SimpleReflectionInitializer reflectionInitializer() {
        return new SimpleReflectionInitializer();
    }

    public SimpleReflectFactory reflectFactory() {
        return new SimpleReflectFactory();
    }

    public Reflect defaultInstance() {
        return reflectFactory().create(ReflectSettings.defaults());
    }
}
