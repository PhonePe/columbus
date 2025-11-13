package com.phonepe.commons.columbus.elastic9.internal.search.query.text;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.misc.text.Fuzziness;
import com.phonepe.commons.columbus.search.misc.text.Terms;
import com.phonepe.commons.columbus.search.query.text.MultiMatchQuery;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MultiMatchQuery.TYPE, type = MultiMatchQuery.class)
public class MultiMatchQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MultiMatchQuery, C> {

    @Override
    public QueryVariant visit(final MultiMatchQuery query, final Object props, final C context) {
        final var builder = new Builder();
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getMatchType(), SearchEnums::toMultiMatchType, builder::type);
        query.getFieldBoosts().entrySet().stream()
                .map(entry -> String.format("%s^%f", entry.getKey(), entry.getValue()))
                .forEach(builder::fields);
        applyIfNotNull(query.getTieBreaker(), Double::valueOf, builder::tieBreaker);
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getTerms(), terms -> populateTerms(builder, terms));
        applyIfNotNull(query.getFuzziness(), fuzziness -> populateFuzziness(builder, fuzziness));
        applyIfNotNull(query.getSlop(), builder::slop);
        applyIfNotNull(query.getIgnoreErrors(), builder::lenient);
        applyIfNotNull(query.getValue(), String::valueOf, builder::query);
        return builder.build();
    }

    private static void populateAnalysis(final Builder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getMaximumExpansions(), query::maxExpansions);
        applyIfNotNull(analysis.getReturnAllForZeroTerms(), SearchEnums::toZeroTermsQuery, query::zeroTermsQuery);
        applyIfNotNull(analysis.getGenerateSynonymQueries(), query::autoGenerateSynonymsPhraseQuery);
    }

    private static void populateTerms(final Builder query, final Terms terms) {
        applyIfNotNull(terms.getMatchAllTerms(), SearchEnums::toOperator, query::operator);
        applyIfNotNull(terms.getMinimumMatch(), query::minimumShouldMatch);
    }

    private static void populateFuzziness(final Builder query, final Fuzziness fuzziness) {
        applyIfNotNull(fuzziness.getAmount(), query::fuzziness);
        applyIfNotNull(fuzziness.getPrefixLength(), query::prefixLength);
        applyIfNotNull(fuzziness.getTransposition(), query::fuzzyTranspositions);
        applyIfNotNull(fuzziness.getRewrite(), query::fuzzyRewrite);
    }
}
