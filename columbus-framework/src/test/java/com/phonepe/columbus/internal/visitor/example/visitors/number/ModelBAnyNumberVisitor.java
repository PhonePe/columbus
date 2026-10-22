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

import com.phonepe.columbus.internal.visitor.example.models.ModelB;
import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "B")
public class ModelBAnyNumberVisitor<P extends Number, C extends AbstractNumberVisitorContext<P, C>>
        implements ModelVisitor<ModelB, P, C> {

    @Override
    public String visit(final ModelB model, final P props, final C context) {
        return "BA:B:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
