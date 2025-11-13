package com.phonepe.commons.columbus.elastic9.internal.storage.toolkit;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.common.initializer.LateInit;
import com.phonepe.commons.columbus.dao.id.IdResolver;
import com.phonepe.commons.columbus.dao.index.IndexResolver;
import com.phonepe.commons.columbus.dao.node.NodeIdResolver;
import com.phonepe.commons.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.commons.columbus.elastic9.dao.index.ElasticIndex;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic9.search.page.ElasticPage;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.commons.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.commons.columbus.search.page.PageResolver;
import com.phonepe.commons.columbus.search.query.QueryTransformer;
import com.phonepe.commons.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.commons.columbus.search.sort.SortTransformer;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.type.TypeResolver;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class ElasticToolkitData implements ElasticToolkit {

    @Getter
    @NonNull
    private final SerDe serDe;

    @Getter
    @NonNull
    private final TypeResolver typeResolver;

    @Getter
    @NonNull
    private final IdResolver<ElasticId, Object> idResolver;

    @Getter
    @NonNull
    private final IndexResolver<ElasticIndex, Object> indexResolver;

    @Getter
    @NonNull
    private final NodeIdResolver<ElasticNode, Object> nodeIdResolver;

    @Getter
    @NonNull
    private final PageResolver<ElasticPage, Object> pageResolver;

    private final LateInit<QueryTransformer<QueryVariant, Object>> queryTransformer = new LateInit<>("Query");

    private final LateInit<SortTransformer<SortOptions, Object>> sortTransformer = new LateInit<>("Sort");

    private final LateInit<AggregatorTransformer<Aggregation, Object>> aggregatorTransformer =
            new LateInit<>("Aggregator");

    private final LateInit<ScoreFunctionTransformer<FunctionScore, Object>> scoreFunctionTransformer =
            new LateInit<>("ScoreFunction");

    private final LateInit<AggregationParserResolver<Aggregate, Object>> aggregationParserResolver =
            new LateInit<>("AggregationParser");

    @Override
    public QueryTransformer<QueryVariant, Object> queryTransformer() {
        return queryTransformer.get();
    }

    @Override
    public SortTransformer<SortOptions, Object> sortTransformer() {
        return sortTransformer.get();
    }

    @Override
    public ScoreFunctionTransformer<FunctionScore, Object> scoreFunctionTransformer() {
        return scoreFunctionTransformer.get();
    }

    @Override
    public AggregatorTransformer<Aggregation, Object> aggregatorTransformer() {
        return aggregatorTransformer.get();
    }

    @Override
    public AggregationParserResolver<Aggregate, Object> aggregationParserResolver() {
        return aggregationParserResolver.get();
    }

    public ElasticToolkitData queryTransformer(final QueryTransformer<QueryVariant, Object> queryTransformer) {
        this.queryTransformer.set(queryTransformer);
        return this;
    }

    public ElasticToolkitData sortTransformer(final SortTransformer<SortOptions, Object> sortTransformer) {
        this.sortTransformer.set(sortTransformer);
        return this;
    }

    public ElasticToolkitData scoreFunctionTransformer(final ScoreFunctionTransformer<FunctionScore, Object> scoreFunctionTransformer) {
        this.scoreFunctionTransformer.set(scoreFunctionTransformer);
        return this;
    }

    public ElasticToolkitData aggregatorTransformer(final AggregatorTransformer<Aggregation, Object> aggregatorTransformer) {
        this.aggregatorTransformer.set(aggregatorTransformer);
        return this;
    }

    public ElasticToolkitData aggregationParserResolver(final AggregationParserResolver<Aggregate, Object> aggregationParserResolver) {
        this.aggregationParserResolver.set(aggregationParserResolver);
        return this;
    }
}
