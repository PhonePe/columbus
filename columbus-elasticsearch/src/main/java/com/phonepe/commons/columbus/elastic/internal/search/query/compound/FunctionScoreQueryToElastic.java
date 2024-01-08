package com.phonepe.commons.columbus.elastic.internal.search.query.compound;

import com.phonepe.commons.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.commons.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.compound.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;

import java.util.Optional;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FunctionScoreQuery.TYPE, type = FunctionScoreQuery.class)
public class FunctionScoreQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<FunctionScoreQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final FunctionScoreQuery query, final Object props, final C context) {
        final var scoreFunctionTransformer = context.toolkit().scoreFunctionTransformer();
        final var functions = query.getFunctions().stream()
                .map(scoreFunction -> scoreFunctionTransformer.transform(scoreFunction, props))
                .toArray(FilterFunctionBuilder[]::new);

        final var builder = Optional.ofNullable(query.getQuery())
                .map(innerQuery -> context.transform(innerQuery, props))
                .map(innerQuery -> QueryBuilders.functionScoreQuery(innerQuery, functions))
                .orElseGet(() -> QueryBuilders.functionScoreQuery(functions));

        applyIfNotNull(query.getMaxBoost(), builder::maxBoost);
        applyIfNotNull(query.getMinScore(), builder::setMinScore);
        applyIfNotNull(query.getBoostMode(), SearchEnums::toBoostMode, builder::boostMode);
        applyIfNotNull(query.getScoreMode(), SearchEnums::toScoreMode, builder::scoreMode);

        return builder;
    }
}
