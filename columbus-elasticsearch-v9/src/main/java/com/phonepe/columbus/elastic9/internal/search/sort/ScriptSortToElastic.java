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
package com.phonepe.columbus.elastic9.internal.search.sort;

import co.elastic.clients.elasticsearch._types.ScriptSort.Builder;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.elastic9.search.sort.SortToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.ScriptSort;

import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = ScriptSort.TYPE, type = ScriptSort.class)
public class ScriptSortToElastic<C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<ScriptSort, C> {

    @Override
    public SortOptions visit(final ScriptSort sort, final Object props, final C context) {
        final var script = Objects.requireNonNull(sort.getScript(), "script must not be null");
        final var valueType = Objects.requireNonNull(sort.getValueType(), "valueType must not be null");

        final var builder = new Builder()
                .script(MiscTransforms.toElastic(script))
                .type(SearchEnums.toScriptSortType(valueType));

        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::mode);

        return new SortOptions.Builder().script(builder.build()).build();
    }
}
