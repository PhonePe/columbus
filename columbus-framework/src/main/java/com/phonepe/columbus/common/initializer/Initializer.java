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
package com.phonepe.columbus.common.initializer;

/**
 * An object that represents a value that can be initialized exactly once.
 *
 * @param <T> the type of the value this object holds.
 */
public interface Initializer<T> {

    /**
     * Get the value held by this object.
     * <p>
     * Use {@link #isPresent()} before calling this method. If the result is false, this method might throw an
     * exception.
     *
     * @return the containing value.
     * @throws IllegalStateException if this is not initialized.
     */
    T get();

    /**
     * Check if this object has a value.
     *
     * @return true if a value could be returned by {@link #get()}, false otherwise.
     */
    boolean isPresent();
}
