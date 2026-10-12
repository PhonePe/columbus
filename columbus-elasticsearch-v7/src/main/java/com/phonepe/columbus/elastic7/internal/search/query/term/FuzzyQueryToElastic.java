package com.phonepe.columbus.elastic7.internal.search.query.term;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.misc.text.Fuzziness;
import com.phonepe.columbus.search.query.term.FuzzyQuery;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FuzzyQuery.TYPE, type = FuzzyQuery.class)
public class FuzzyQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<FuzzyQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final FuzzyQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.fuzzyQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getFuzziness(), fuzziness -> populateFuzziness(builder, fuzziness));
        applyIfNotNull(query.getMaximumExpansions(), builder::maxExpansions);
        return builder;
    }

    private static void populateFuzziness(final FuzzyQueryBuilder query, final Fuzziness fuzziness) {
        applyIfNotNull(fuzziness.getAmount(), MiscTransforms::toElasticFuzziness, query::fuzziness);
        applyIfNotNull(fuzziness.getPrefixLength(), query::prefixLength);
        applyIfNotNull(fuzziness.getTransposition(), query::transpositions);
        applyIfNotNull(fuzziness.getRewrite(), query::rewrite);
    }
}
