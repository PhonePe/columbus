package com.phonepe.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.query_dsl.DateRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeRelation;
import co.elastic.clients.elasticsearch._types.query_dsl.TermRangeQuery;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.RangeQuery;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = RangeQuery.TYPE, type = RangeQuery.class)
public class RangeQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<RangeQuery, C> {

    @Override
    public QueryVariant visit(final RangeQuery query, final Object props, final C context) {
        if (query.getTimezone() != null || query.getFormat() != null) {
            return getDateRangeQuery(query);
        } else {
            if (query.getFrom() instanceof Number || query.getTo() instanceof Number) {
                return getNumericRangeQuery(query);
            } else {
                return getTermsRangeQuery(query);
            }
        }
    }

    private QueryVariant getDateRangeQuery(final RangeQuery query) {
        if (query.getFrom() instanceof String from && query.getTo() instanceof String to) {
            final var builder = new DateRangeQuery.Builder()
                    .field(query.getField());

            applyCommonProperties(query, builder);
            applyIfNotNull(from, query.includeLower() ? builder::gte : builder::gt);
            applyIfNotNull(to, query.includeUpper() ? builder::lte : builder::lt);
            applyIfNotNull(query.getTimezone(), builder::timeZone);
            applyIfNotNull(query.getFormat(), builder::format);
            applyIfNotNull(query.getRelation(), RangeRelation::valueOf, builder::relation);

            return builder.build()._toRangeQuery();
        } else {
            throw new IllegalArgumentException("For date range queries, 'from' and 'to' must be of type String");
        }
    }

    private QueryVariant getNumericRangeQuery(final RangeQuery query) {
        final var builder = new NumberRangeQuery.Builder()
                .field(query.getField());

        applyCommonProperties(query, builder);

        if (query.getFrom() instanceof Number from) {
            applyIfNotNull(from.doubleValue(), query.includeLower() ? builder::gte : builder::gt);
        }

        if (query.getTo() instanceof Number to) {
            applyIfNotNull(to.doubleValue(), query.includeUpper() ? builder::lte : builder::lt);
        }

        applyIfNotNull(query.getRelation(), RangeRelation::valueOf, builder::relation);
        return builder.build()._toRangeQuery();
    }

    private QueryVariant getTermsRangeQuery(final RangeQuery query) {
        final String from = (String) query.getFrom();
        final String to = (String) query.getTo();

        final var builder = new TermRangeQuery.Builder()
                .field(query.getField());

        applyCommonProperties(query, builder);
        applyIfNotNull(from, query.includeLower() ? builder::gte : builder::gt);
        applyIfNotNull(to, query.includeUpper() ? builder::lte : builder::lt);
        applyIfNotNull(query.getRelation(), RangeRelation::valueOf, builder::relation);

        return builder.build()._toRangeQuery();
    }
}
