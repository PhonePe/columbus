package com.phonepe.commons.columbus.elastic.internal.search.query.text;

import com.phonepe.commons.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.commons.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.query.text.MatchPhrasePrefixQuery;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MatchPhrasePrefixQuery.TYPE, type = MatchPhrasePrefixQuery.class)
public class MatchPhrasePrefixQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<MatchPhrasePrefixQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchPhrasePrefixQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.matchPhrasePrefixQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getSlop(), builder::slop);
        return builder;
    }

    private static void populateAnalysis(final MatchPhrasePrefixQueryBuilder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getMaximumExpansions(), query::maxExpansions);
        applyIfNotNull(analysis.getReturnAllForZeroTerms(), SearchEnums::toZeroTermsQuery, query::zeroTermsQuery);
    }
}
