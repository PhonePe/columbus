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
package com.phonepe.columbus.elastic9.internal.search.retriever;

import co.elastic.clients.elasticsearch._types.Retriever;
import co.elastic.clients.elasticsearch._types.StandardRetriever.Builder;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.retriever.StandardRetriever;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = StandardRetriever.TYPE, type = StandardRetriever.class)
public class StandardRetrieverToElastic<C extends ElasticRetrieverTransformationContext<C>>
        implements RetrieverToElastic<StandardRetriever, C> {

    @Override
    public Retriever visit(final StandardRetriever retriever, final Object props, final C context) {
        final var toolkit = context.toolkit();
        final var queryVariant = toolkit.queryTransformer().transform(retriever.getQuery(), props);

        final var builder = new Builder().query(queryVariant);

        applyIfNotNull(retriever.getSort(), sort -> toolkit.sortTransformer().transform(sort, props),
                builder::sort);

        applyIfNotNull(retriever.getCollapse(), collapse -> MiscTransforms.toElastic(collapse, toolkit),
                builder::collapse);

        applyIfNotNull(retriever.getMinScore(), builder::minScore);

        retriever.getFilterClauses().stream().map(filter -> toolkit.queryTransformer()
                        .transform(filter, props))
                .forEach(builder::filter);

        return new Retriever.Builder().standard(builder.build()).build();
    }
}
