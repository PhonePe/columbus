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
package com.phonepe.columbus.internal.core.toolkit;

import com.phonepe.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.type.TypeResolver;
import com.phonepe.columbus.visitor.factory.VisitorRegistryFactory;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * A concrete implementation of {@link Toolkit} ensuring all the fields are initialized with valid values.
 */
@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class ToolkitData implements Toolkit {

    @NonNull
    private final Reflect reflect;

    @NonNull
    private final SerDe serDe;

    @NonNull
    private final TypeResolver typeResolver;

    @NonNull
    private final RegistryFactory registryFactory;

    @NonNull
    private final VisitorRegistryFactory visitorFactory;

    @NonNull
    private final HandlerRegistryFactory handlerFactory;
}
