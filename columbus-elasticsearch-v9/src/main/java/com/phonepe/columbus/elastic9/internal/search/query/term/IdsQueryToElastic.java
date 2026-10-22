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
package com.phonepe.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.IdsQuery;

@RegistryKey(name = IdsQuery.TYPE, type = IdsQuery.class)
public class IdsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<IdsQuery, C> {

    @Override
    public QueryVariant visit(final IdsQuery query, final Object props, final C context) {
        final var idResolver = context.toolkit().idResolver();
        final var builder = new Builder()
                .values(query.getIds().stream()
                        .map(id -> idResolver.resolve(id, props))
                        .map(ElasticId::getId).toList());

        applyCommonProperties(query, builder);
        return builder.build();
    }
}
