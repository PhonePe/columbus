package com.phonepe.columbus.elastic7.internal.search.query.term;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.RangeQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = RangeQuery.TYPE, type = RangeQuery.class)
public class RangeQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<RangeQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final RangeQuery query, final Object props, final C context) {
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
