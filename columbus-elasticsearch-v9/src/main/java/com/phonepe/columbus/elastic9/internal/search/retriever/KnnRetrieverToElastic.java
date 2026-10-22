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

import co.elastic.clients.elasticsearch._types.KnnRetriever.Builder;
import co.elastic.clients.elasticsearch._types.RescoreVector;
import co.elastic.clients.elasticsearch._types.Retriever;
import com.phonepe.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.retriever.KnnRetriever;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = KnnRetriever.TYPE, type = KnnRetriever.class)
public class KnnRetrieverToElastic<C extends ElasticRetrieverTransformationContext<C>>
        implements RetrieverToElastic<KnnRetriever, C> {

    @Override
    public Retriever visit(final KnnRetriever retriever, final Object props, final C context) {
        final var toolkit = context.toolkit();
        final var builder = new Builder()
                .field(retriever.getField())
                .queryVector(retriever.getQueryVector());

        applyIfNotNull(retriever.getK(), builder::k);
        applyIfNotNull(retriever.getNumCandidates(), builder::numCandidates);
        applyIfNotNull(retriever.getSimilarity(), builder::similarity);
        applyIfNotNull(retriever.getOversample(),
                oversample -> new RescoreVector.Builder()
                        .oversample(oversample)
                        .build(), builder::rescoreVector);

        retriever.getFilterClauses().stream().map(filter -> toolkit.queryTransformer()
                        .transform(filter, props))
                .forEach(builder::filter);

        return new Retriever.Builder().knn(builder.build()).build();
    }
}
