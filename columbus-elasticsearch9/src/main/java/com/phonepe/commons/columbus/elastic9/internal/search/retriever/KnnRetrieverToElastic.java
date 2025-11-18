package com.phonepe.commons.columbus.elastic9.internal.search.retriever;

import co.elastic.clients.elasticsearch._types.KnnRetriever.Builder;
import co.elastic.clients.elasticsearch._types.RescoreVector;
import co.elastic.clients.elasticsearch._types.Retriever;
import com.phonepe.commons.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.commons.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.retriever.KnnRetriever;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

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
