package com.phonepe.platform.columbus.elastic.internal.search.query.compound;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.Query;
import com.phonepe.platform.columbus.search.query.compound.BoolQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.function.Consumer;

@RegistryKey(name = BoolQuery.TYPE, type = BoolQuery.class)
public class BoolQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<BoolQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final BoolQuery query, final C context) {
        final var builder = QueryBuilders.boolQuery();

        convertAll(context, query.getMustClauses(), builder::must);
        convertAll(context, query.getShouldClauses(), builder::should);
        convertAll(context, query.getFilterClauses(), builder::filter);
        convertAll(context, query.getMustNotClauses(), builder::mustNot);

        return builder.minimumShouldMatch(query.getMinimumMatch());
    }

    private void convertAll(final C context, final List<Query> queries, final Consumer<QueryBuilder> collector) {
        queries.stream()
                .map(context::transform)
                .forEachOrdered(collector);
    }
}
