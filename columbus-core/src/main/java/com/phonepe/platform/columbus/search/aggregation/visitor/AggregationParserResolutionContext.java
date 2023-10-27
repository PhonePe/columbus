package com.phonepe.platform.columbus.search.aggregation.visitor;

import com.phonepe.platform.columbus.search.aggregation.AggregationParser;
import com.phonepe.platform.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.platform.columbus.search.aggregator.Aggregator;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link AggregationParserResolver} that uses visitor to resolve the {@link AggregationParser} for a given
 * {@link Aggregator}.
 *
 * @param <R> the type used by storage for aggregation results.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class AggregationParserResolutionContext<R, C extends AggregationParserResolutionContext<R, C>>
        extends VisitorContext<AggregationParser<R>, Aggregator, C> implements AggregationParserResolver<R> {

    @Override
    public AggregationParser<R> resolve(final Aggregator aggregator) {
        return visit(aggregator);
    }
}
