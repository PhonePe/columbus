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

/**
 * A factory to initialize an instance from its {@link Class}.
 */
public interface InstanceFactory {

    /**
     * Creates an instance of the given class.
     *
     * @param type the class to create an instance of.
     * @param <T>  the actual type of the instance.
     * @return the created instance.
     * @throws IllegalArgumentException if the type cannot be instantiated.
     * @throws NullPointerException     if the type is null.
     */
    <T> T getInstance(Class<T> type);
}
