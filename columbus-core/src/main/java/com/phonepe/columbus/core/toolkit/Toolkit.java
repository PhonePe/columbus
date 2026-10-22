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
package com.phonepe.columbus.core.toolkit;

import com.phonepe.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.type.TypeResolver;
import com.phonepe.columbus.visitor.factory.VisitorRegistryFactory;

/**
 * A collection of all the building blocks used by Columbus.
 */
public interface Toolkit {

    Reflect reflect();

    SerDe serDe();

    TypeResolver typeResolver();

    RegistryFactory registryFactory();

    VisitorRegistryFactory visitorFactory();

    HandlerRegistryFactory handlerFactory();
}
