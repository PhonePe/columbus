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
package com.phonepe.columbus.internal.visitor.example.visitors.number;

import com.phonepe.columbus.internal.visitor.example.models.ModelA;
import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "A")
public class ModelANumberVisitor<C extends ModelVisitorContext<Number, C>> implements ModelVisitor<ModelA, Number, C> {

    @Override
    public String visit(final ModelA model, final Number props, final C context) {
        return "AN:A:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
