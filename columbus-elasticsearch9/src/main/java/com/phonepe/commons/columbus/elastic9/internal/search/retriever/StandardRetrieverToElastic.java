package com.phonepe.commons.columbus.elastic9.internal.search.retriever;

import co.elastic.clients.elasticsearch._types.Retriever;
import co.elastic.clients.elasticsearch._types.StandardRetriever.Builder;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import com.phonepe.commons.columbus.elastic9.search.retriever.RetrieverToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.retriever.StandardRetriever;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

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
