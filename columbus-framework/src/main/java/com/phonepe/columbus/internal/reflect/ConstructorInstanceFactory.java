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
package com.phonepe.columbus.internal.reflect;

import com.phonepe.columbus.reflect.InstanceFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Uses the default constructor to create an instance.
 */
@Slf4j
public class ConstructorInstanceFactory implements InstanceFactory {

    @Override
    public <T> T getInstance(final Class<T> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to create instance for {}", type, e);
            throw new IllegalArgumentException("Failed to create instance for " + type.getName(), e);
        }
    }
}
