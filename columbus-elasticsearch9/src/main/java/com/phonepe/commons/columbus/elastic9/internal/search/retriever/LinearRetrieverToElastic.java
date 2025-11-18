package com.phonepe.commons.columbus.elastic9.internal.search.retriever;

import co.elastic.clients.elasticsearch._types.InnerRetriever;
import co.elastic.clients.elasticsearch._types.LinearRetriever.Builder;
import co.elastic.clients.elasticsearch._types.Retriever;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.commons.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.retriever.LinearRetriever;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

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
