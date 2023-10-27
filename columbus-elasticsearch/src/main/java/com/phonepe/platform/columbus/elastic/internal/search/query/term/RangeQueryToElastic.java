package com.phonepe.platform.columbus.elastic.internal.search.query.term;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.term.RangeQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = RangeQuery.TYPE, type = RangeQuery.class)
public class RangeQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<RangeQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final RangeQuery query, final C context) {
        final var builder = QueryBuilders.rangeQuery(query.getField())
                .includeLower(query.includeLower())
                .includeUpper(query.includeUpper());

        applyIfNotNull(query.getFrom(), builder::from);
        applyIfNotNull(query.getTo(), builder::to);
        applyIfNotNull(query.getTimezone(), builder::timeZone);
        applyIfNotNull(query.getFormat(), builder::format);
        applyIfNotNull(query.getRelation(), builder::relation);

        return builder;
    }
}
