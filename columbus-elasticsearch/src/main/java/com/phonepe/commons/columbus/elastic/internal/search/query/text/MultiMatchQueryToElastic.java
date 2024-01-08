package com.phonepe.commons.columbus.elastic.internal.search.query.text;

import com.phonepe.commons.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.misc.text.Fuzziness;
import com.phonepe.commons.columbus.search.misc.text.Terms;
import com.phonepe.commons.columbus.search.query.text.MultiMatchQuery;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MultiMatchQuery.TYPE, type = MultiMatchQuery.class)
public class MultiMatchQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<MultiMatchQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MultiMatchQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.multiMatchQuery(query.getValue());
        applyIfNotNull(query.getMatchType(), SearchEnums::toMultiMatchType, builder::type);
        query.getFieldBoosts().forEach(builder::field);
        applyIfNotNull(query.getTieBreaker(), builder::tieBreaker);
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getTerms(), terms -> populateTerms(builder, terms));
        applyIfNotNull(query.getFuzziness(), fuzziness -> populateFuzziness(builder, fuzziness));
        applyIfNotNull(query.getSlop(), builder::slop);
        applyIfNotNull(query.getIgnoreErrors(), builder::lenient);
        return builder;
    }

    private static void populateAnalysis(final MultiMatchQueryBuilder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getMaximumExpansions(), query::maxExpansions);
        applyIfNotNull(analysis.getReturnAllForZeroTerms(), SearchEnums::toZeroTermsQuery, query::zeroTermsQuery);
        applyIfNotNull(analysis.getGenerateSynonymQueries(), query::autoGenerateSynonymsPhraseQuery);
    }

    private static void populateTerms(final MultiMatchQueryBuilder query, final Terms terms) {
        applyIfNotNull(terms.getMatchAllTerms(), SearchEnums::toOperator, query::operator);
        applyIfNotNull(terms.getMinimumMatch(), query::minimumShouldMatch);
    }

    private static void populateFuzziness(final MultiMatchQueryBuilder query, final Fuzziness fuzziness) {
        applyIfNotNull(fuzziness.getAmount(), MiscTransforms::toElasticFuzziness, query::fuzziness);
        applyIfNotNull(fuzziness.getPrefixLength(), query::prefixLength);
        applyIfNotNull(fuzziness.getTransposition(), query::fuzzyTranspositions);
        applyIfNotNull(fuzziness.getRewrite(), query::fuzzyRewrite);
    }
}
