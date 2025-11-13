package com.phonepe.commons.columbus.elastic9.internal.search.query.compound;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.query.compound.BoolQuery;

import java.util.List;
import java.util.function.Consumer;

@RegistryKey(name = BoolQuery.TYPE, type = BoolQuery.class)
public class BoolQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<BoolQuery, C> {

    @Override
    public QueryVariant visit(final BoolQuery query, final Object props, final C context) {
        final var builder = new Builder();
        applyCommonProperties(query, builder);

        convertAll(context, props, query.getMustClauses(), builder::must);
        convertAll(context, props, query.getShouldClauses(), builder::should);
        convertAll(context, props, query.getFilterClauses(), builder::filter);
        convertAll(context, props, query.getMustNotClauses(), builder::mustNot);

        builder.minimumShouldMatch(query.getMinimumMatch());

        return builder.build();
    }

    private void convertAll(
            final C context,
            final Object props,
            final List<Query> queries,
            final Consumer<QueryVariant> collector
    ) {
        queries.stream()
                .map(query -> context.transform(query, props))
                .forEachOrdered(collector);
    }
}
