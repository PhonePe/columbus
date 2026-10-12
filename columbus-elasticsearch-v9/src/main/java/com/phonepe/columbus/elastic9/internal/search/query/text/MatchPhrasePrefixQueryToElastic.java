package com.phonepe.columbus.elastic9.internal.search.query.text;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.misc.text.Analysis;
import com.phonepe.columbus.search.query.text.MatchPhrasePrefixQuery;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MatchPhrasePrefixQuery.TYPE, type = MatchPhrasePrefixQuery.class)
public class MatchPhrasePrefixQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MatchPhrasePrefixQuery, C> {

    @Override
    public QueryVariant visit(final MatchPhrasePrefixQuery query, final Object props, final C context) {
        final var builder = new Builder().field(query.getField()).query(query.getValue().toString());
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getSlop(), builder::slop);
        return builder.build();
    }

    private static void populateAnalysis(final Builder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getMaximumExpansions(), query::maxExpansions);
        applyIfNotNull(analysis.getReturnAllForZeroTerms(), SearchEnums::toZeroTermsQuery, query::zeroTermsQuery);
    }
}
