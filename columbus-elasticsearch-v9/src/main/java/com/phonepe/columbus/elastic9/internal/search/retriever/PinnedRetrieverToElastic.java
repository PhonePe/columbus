package com.phonepe.columbus.elastic9.internal.search.retriever;

import co.elastic.clients.elasticsearch._types.PinnedRetriever.Builder;
import co.elastic.clients.elasticsearch._types.Retriever;
import com.phonepe.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.retriever.PinnedRetriever;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = PinnedRetriever.TYPE, type = PinnedRetriever.class)
public class PinnedRetrieverToElastic<C extends ElasticRetrieverTransformationContext<C>>
        implements RetrieverToElastic<PinnedRetriever, C> {

    @Override
    public Retriever visit(final PinnedRetriever retriever, final Object props, final C context) {
        final var toolkit = context.toolkit();
        final var builder = new Builder();

        applyIfNotNull(retriever.getIds(), builder::ids);
        applyIfNotNull(retriever.getRetriever(), pinnedRetriever ->
                toolkit.retrieverTransformer().transform(pinnedRetriever, props), builder::retriever);

        return new Retriever.Builder().pinned(builder.build()).build();
    }
}
