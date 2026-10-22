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

import co.elastic.clients.elasticsearch._types.InnerRetriever;
import co.elastic.clients.elasticsearch._types.LinearRetriever.Builder;
import co.elastic.clients.elasticsearch._types.Retriever;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.retriever.LinearRetriever;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = LinearRetriever.TYPE, type = LinearRetriever.class)
public class LinearRetrieverToElastic<C extends ElasticRetrieverTransformationContext<C>>
        implements RetrieverToElastic<LinearRetriever, C> {

    @Override
    public Retriever visit(final LinearRetriever retriever, final Object props, final C context) {
        final var toolkit = context.toolkit();
        final var normalizer = SearchEnums.toScoreNormalizer(retriever.getNormalizer());

        final var builder = new Builder().normalizer(normalizer);

        retriever.getRetrievers().forEach((ret, weight) ->
                builder.retrievers(new InnerRetriever.Builder()
                        .retriever(toolkit.retrieverTransformer().transform(ret, props))
                        .weight(weight)
                        .normalizer(normalizer)
                        .build()));

        retriever.getFilterClauses().stream().map(filter -> toolkit.queryTransformer()
                        .transform(filter, props))
                .forEach(builder::filter);

        applyIfNotNull(retriever.getRankWindowSize(), builder::rankWindowSize);

        return new Retriever.Builder().linear(builder.build()).build();
    }
}
