package com.phonepe.commons.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.misc.text.Fuzziness;
import com.phonepe.commons.columbus.search.query.term.FuzzyQuery;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FuzzyQuery.TYPE, type = FuzzyQuery.class)
public class FuzzyQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<FuzzyQuery, C> {

    @Override
    public QueryVariant visit(final FuzzyQuery query, final Object props, final C context) {
        final var builder = new Builder().field(query.getField());
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getFuzziness(), fuzziness -> populateFuzziness(builder, fuzziness));
        applyIfNotNull(query.getMaximumExpansions(), builder::maxExpansions);
        applyIfNotNull(query.getValue(), FieldValue::of, builder::value);
        return builder.build();
    }

    private static void populateFuzziness(final Builder query,
                                          final Fuzziness fuzziness) {
        applyIfNotNull(fuzziness.getAmount(), query::fuzziness);
        applyIfNotNull(fuzziness.getPrefixLength(), query::prefixLength);
        applyIfNotNull(fuzziness.getTransposition(), query::transpositions);
        applyIfNotNull(fuzziness.getRewrite(), query::rewrite);
    }
}
