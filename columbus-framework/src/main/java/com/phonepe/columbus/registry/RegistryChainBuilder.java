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
package com.phonepe.columbus.registry;

/**
 * An object that helps to build a chain of registries.
 * <p>
 * The builder can choose to retain the structure or flatten the inner registries.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface RegistryChainBuilder<K, V> {

    RegistryChainBuilder<K, V> add(Registry<? super K, ? extends V> registry);

    RegistryChain<K, V> build();
}
