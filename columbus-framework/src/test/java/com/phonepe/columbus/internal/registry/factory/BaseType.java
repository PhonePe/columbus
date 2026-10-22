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
package com.phonepe.columbus.internal.registry.factory;

import com.phonepe.columbus.registry.factory.RegistryKey;

interface BaseType {

    interface SubType extends BaseType {}

    @RegistryKey(name = "A")
    @RegistryKey(type = BaseType.class, priority = 20)
    class TypeA implements BaseType {
    }

    @RegistryKey(name = "B", type = BaseType.class, priority = 10)
    class TypeB implements BaseType {
    }

    @RegistryKey(name = "A", type = SubType.class)
    class TypeA2 implements SubType {
    }

    @RegistryKey(name = "B")
    class TypeB2 implements SubType {}
}
