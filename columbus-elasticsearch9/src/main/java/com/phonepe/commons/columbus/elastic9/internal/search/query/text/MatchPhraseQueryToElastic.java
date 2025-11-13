package com.phonepe.commons.columbus.elastic9.internal.search.query.text;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.query.text.MatchPhraseQuery;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MatchPhraseQuery.TYPE, type = MatchPhraseQuery.class)
public class MatchPhraseQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MatchPhraseQuery, C> {

    @Override
    public QueryVariant visit(final MatchPhraseQuery query, final Object props, final C context) {
        final var builder = new Builder().field(query.getField()).query(query.getValue().toString());
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getSlop(), builder::slop);
        return builder.build();
    }

    private static void populateAnalysis(final Builder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getReturnAllForZeroTerms(), SearchEnums::toZeroTermsQuery, query::zeroTermsQuery);
    }
}
