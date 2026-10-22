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
package com.phonepe.columbus.elastic9.internal.search.query.special;

import co.elastic.clients.elasticsearch._types.KnnQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.special.KnnQuery;

import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = KnnQuery.TYPE, type = KnnQuery.class)
public class KnnQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<KnnQuery, C> {

    @Override
    public QueryVariant visit(final KnnQuery query, final Object props, final C context) {
        final var toolkit = context.toolkit();
        final var builder = new Builder().field(query.getField()).queryVector(query.getQueryVector());

        applyCommonProperties(query, builder);
        applyIfNotNull(query.getK(), builder::k);
        applyIfNotNull(query.getNumCandidates(), builder::numCandidates);
        applyIfNotNull(query.getBoost(), builder::boost);
        applyIfNotNull(query.getSimilarity(), builder::similarity);
        applyIfNotNull(query.getOversample(), MiscTransforms::toElasticRescoreVector, builder::rescoreVector);

        if (query.getFilterClauses() != null) {
            query.getFilterClauses().stream()
                    .map(toolkit.queryTransformer()::transform)
                    .filter(Objects::nonNull)
                    .forEach(builder::filter);
        }

        return builder.build();
    }
}
