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
