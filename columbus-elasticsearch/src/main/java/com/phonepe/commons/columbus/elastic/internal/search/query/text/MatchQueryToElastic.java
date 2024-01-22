package com.phonepe.commons.columbus.elastic.internal.search.query.text;

import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.misc.text.Fuzziness;
import com.phonepe.commons.columbus.search.misc.text.Terms;
import com.phonepe.commons.columbus.search.query.text.MatchQuery;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MatchQuery.TYPE, type = MatchQuery.class)
public class MatchQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MatchQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.matchQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getTerms(), terms -> populateTerms(builder, terms));
        applyIfNotNull(query.getFuzziness(), fuzziness -> populateFuzziness(builder, fuzziness));
        applyIfNotNull(query.getIgnoreErrors(), builder::lenient);
        return builder;
    }

    private static void populateAnalysis(final MatchQueryBuilder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getMaximumExpansions(), query::maxExpansions);
        applyIfNotNull(analysis.getReturnAllForZeroTerms(), SearchEnums::toZeroTermsQuery, query::zeroTermsQuery);
        applyIfNotNull(analysis.getGenerateSynonymQueries(), query::autoGenerateSynonymsPhraseQuery);
    }

    private static void populateTerms(final MatchQueryBuilder query, final Terms terms) {
        applyIfNotNull(terms.getMatchAllTerms(), SearchEnums::toOperator, query::operator);
        applyIfNotNull(terms.getMinimumMatch(), query::minimumShouldMatch);
    }

    private static void populateFuzziness(final MatchQueryBuilder query, final Fuzziness fuzziness) {
        applyIfNotNull(fuzziness.getAmount(), MiscTransforms::toElasticFuzziness, query::fuzziness);
        applyIfNotNull(fuzziness.getPrefixLength(), query::prefixLength);
        applyIfNotNull(fuzziness.getTransposition(), query::fuzzyTranspositions);
        applyIfNotNull(fuzziness.getRewrite(), query::fuzzyRewrite);
    }
}
