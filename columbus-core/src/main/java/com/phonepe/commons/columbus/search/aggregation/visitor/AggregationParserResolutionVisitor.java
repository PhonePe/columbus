package com.phonepe.commons.columbus.search.aggregation.visitor;

import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import com.phonepe.commons.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link AggregationParser} to parse the internal representation used by the
 * storage for aggregation results.
 *
 * @param <R> the type used by storage for aggregation results.
 * @param <T> the type of the {@link Aggregator} whose result is to be parsed.
 * @param <P> represents the additional context required for resolution.
 * @param <C> the type of the context required for resolution.
 */
public interface AggregationParserResolutionVisitor<R, T extends Aggregator, P,
        C extends AggregationParserResolutionContext<R, P, C>>
        extends EntityVisitor<AggregationParser<R>, T, Aggregator, P, C> {

    @Override
    AggregationParser<R> visit(T aggregator, P props, C context);
}
