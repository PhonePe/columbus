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

import co.elastic.clients.elasticsearch._types.FieldSort.Builder;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.elastic9.search.sort.SortToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.FieldSort;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FieldSort.TYPE, type = FieldSort.class)
public class FieldSortToElastic<C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<FieldSort, C> {

    @Override
    public SortOptions visit(final FieldSort sort, final Object props, final C context) {
        final var builder = new Builder().field(sort.getField());
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::mode);
        applyIfNotNull(sort.getNumericType(), SearchEnums::toFieldSortNumericType, builder::numericType);
        applyIfNotNull(sort.getFallbackFieldType(), SearchEnums::toUnmappedType, builder::unmappedType);
        applyIfNotNull(sort.getMissingValue(), FieldValue::of, builder::missing);
        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        return new SortOptions.Builder().field(builder.build()).build();
    }
}
