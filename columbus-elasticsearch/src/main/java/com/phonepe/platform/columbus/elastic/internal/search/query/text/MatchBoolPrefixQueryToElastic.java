package com.phonepe.platform.columbus.elastic.internal.search.query.text;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.platform.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.misc.text.Analysis;
import com.phonepe.platform.columbus.search.misc.text.Fuzziness;
import com.phonepe.platform.columbus.search.misc.text.Terms;
import com.phonepe.platform.columbus.search.query.text.MatchBoolPrefixQuery;
import org.elasticsearch.index.query.MatchBoolPrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = MatchBoolPrefixQuery.TYPE, type = MatchBoolPrefixQuery.class)
public class MatchBoolPrefixQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<MatchBoolPrefixQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchBoolPrefixQuery query, final C context) {
        final var builder = QueryBuilders.matchBoolPrefixQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getAnalysis(), analysis -> populateAnalysis(builder, analysis));
        applyIfNotNull(query.getTerms(), terms -> populateTerms(builder, terms));
        applyIfNotNull(query.getFuzziness(), fuzziness -> populateFuzziness(builder, fuzziness));
        return builder;
    }

    private static void populateAnalysis(final MatchBoolPrefixQueryBuilder query, final Analysis analysis) {
        applyIfNotNull(analysis.getAnalyzer(), query::analyzer);
        applyIfNotNull(analysis.getMaximumExpansions(), query::maxExpansions);
    }

    private static void populateTerms(final MatchBoolPrefixQueryBuilder query, final Terms terms) {
        applyIfNotNull(terms.getMatchAllTerms(), SearchEnums::toOperator, query::operator);
        applyIfNotNull(terms.getMinimumMatch(), query::minimumShouldMatch);
    }

    private static void populateFuzziness(final MatchBoolPrefixQueryBuilder query, final Fuzziness fuzziness) {
        applyIfNotNull(fuzziness.getAmount(), MiscTransforms::toElasticFuzziness, query::fuzziness);
        applyIfNotNull(fuzziness.getPrefixLength(), query::prefixLength);
        applyIfNotNull(fuzziness.getTransposition(), query::fuzzyTranspositions);
        applyIfNotNull(fuzziness.getRewrite(), query::fuzzyRewrite);
    }
}
