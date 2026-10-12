package com.phonepe.columbus.elastic7.internal.search.query.compound;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.compound.BoolQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.function.Consumer;

@RegistryKey(name = BoolQuery.TYPE, type = BoolQuery.class)
public class BoolQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<BoolQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final BoolQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.boolQuery();

        convertAll(context, props, query.getMustClauses(), builder::must);
        convertAll(context, props, query.getShouldClauses(), builder::should);
        convertAll(context, props, query.getFilterClauses(), builder::filter);
        convertAll(context, props, query.getMustNotClauses(), builder::mustNot);

        return builder.minimumShouldMatch(query.getMinimumMatch());
    }

    private void convertAll(
            final C context,
            final Object props,
            final List<Query> queries,
            final Consumer<QueryBuilder> collector
    ) {
        queries.stream()
                .map(query -> context.transform(query, props))
                .forEachOrdered(collector);
    }
}
