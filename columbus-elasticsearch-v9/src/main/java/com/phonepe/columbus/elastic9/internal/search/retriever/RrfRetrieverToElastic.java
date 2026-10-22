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

import co.elastic.clients.elasticsearch._types.RRFRetriever.Builder;
import co.elastic.clients.elasticsearch._types.Retriever;
import com.phonepe.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.retriever.RrfRetriever;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = RrfRetriever.TYPE, type = RrfRetriever.class)
public class RrfRetrieverToElastic<C extends ElasticRetrieverTransformationContext<C>>
        implements RetrieverToElastic<RrfRetriever, C> {

    @Override
    public Retriever visit(final RrfRetriever rrfRetriever, final Object props, final C context) {
        final var toolkit = context.toolkit();
        final var builder = new Builder();

        rrfRetriever.getRetrievers().stream().map(retriever -> toolkit.retrieverTransformer()
                        .transform(retriever, props))
                .forEach(builder::retrievers);

        rrfRetriever.getFilterClauses().stream().map(filter -> toolkit.queryTransformer()
                        .transform(filter, props))
                .forEach(builder::filter);

        applyIfNotNull(rrfRetriever.getRankConstant(), builder::rankConstant);
        applyIfNotNull(rrfRetriever.getRankWindowSize(), builder::rankWindowSize);

        return new Retriever.Builder().rrf(builder.build()).build();
    }
}
