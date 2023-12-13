package com.phonepe.platform.columbus.elastic.internal.search.query.text;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.misc.text.Analysis;
import com.phonepe.platform.columbus.search.query.text.MatchPhraseQuery;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MatchPhraseQuery.TYPE, type = MatchPhraseQuery.class)
public class MatchPhraseQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<MatchPhraseQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchPhraseQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.matchPhraseQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getSlop(), builder::slop);
        return builder;
    }

    private static void populateAnalysis(final MatchPhraseQueryBuilder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getReturnAllForZeroTerms(), SearchEnums::toZeroTermsQuery, query::zeroTermsQuery);
    }
}
