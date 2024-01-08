package com.phonepe.commons.columbus.elastic.internal.storage.toolkit;

import com.phonepe.commons.columbus.common.initializer.LateInit;
import com.phonepe.commons.columbus.dao.id.IdResolver;
import com.phonepe.commons.columbus.dao.index.IndexResolver;
import com.phonepe.commons.columbus.dao.node.NodeIdResolver;
import com.phonepe.commons.columbus.elastic.dao.id.ElasticId;
import com.phonepe.commons.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.commons.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic.search.page.ElasticPage;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.commons.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.commons.columbus.search.page.PageResolver;
import com.phonepe.commons.columbus.search.query.QueryTransformer;
import com.phonepe.commons.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.commons.columbus.search.sort.SortTransformer;
import com.phonepe.commons.columbus.serde.SerDe;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class ElasticToolkitData implements ElasticToolkit {

    @Getter
    @NonNull
    private final SerDe serDe;

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

    private final LateInit<QueryTransformer<QueryBuilder, Object>> queryTransformer = new LateInit<>("Query");

    private final LateInit<SortTransformer<SortBuilder<?>, Object>> sortTransformer = new LateInit<>("Sort");

    private final LateInit<ScoreFunctionTransformer<FilterFunctionBuilder, Object>> scoreFunctionTransformer =
            new LateInit<>("ScoreFunction");

    private final LateInit<AggregatorTransformer<AggregationBuilder, Object>> aggregatorTransformer =
            new LateInit<>("Aggregator");

    private final LateInit<AggregationParserResolver<Aggregation, Object>> aggregationParserResolver =
            new LateInit<>("AggregationParser");

    @Override
    public QueryTransformer<QueryBuilder, Object> queryTransformer() {
        return queryTransformer.get();
    }

    @Override
    public SortTransformer<SortBuilder<?>, Object> sortTransformer() {
        return sortTransformer.get();
    }

    @Override
    public ScoreFunctionTransformer<FilterFunctionBuilder, Object> scoreFunctionTransformer() {
        return scoreFunctionTransformer.get();
    }

    @Override
    public AggregatorTransformer<AggregationBuilder, Object> aggregatorTransformer() {
        return aggregatorTransformer.get();
    }

    @Override
    public AggregationParserResolver<Aggregation, Object> aggregationParserResolver() {
        return aggregationParserResolver.get();
    }

    public ElasticToolkitData queryTransformer(final QueryTransformer<QueryBuilder, Object> queryTransformer) {
        this.queryTransformer.set(queryTransformer);
        return this;
    }

    public ElasticToolkitData sortTransformer(final SortTransformer<SortBuilder<?>, Object> sortTransformer) {
        this.sortTransformer.set(sortTransformer);
        return this;
    }

    public ElasticToolkitData scoreFunctionTransformer(final ScoreFunctionTransformer<FilterFunctionBuilder, Object> scoreFunctionTransformer) {
        this.scoreFunctionTransformer.set(scoreFunctionTransformer);
        return this;
    }

    public ElasticToolkitData aggregatorTransformer(final AggregatorTransformer<AggregationBuilder, Object> aggregatorTransformer) {
        this.aggregatorTransformer.set(aggregatorTransformer);
        return this;
    }

    public ElasticToolkitData aggregationParserResolver(final AggregationParserResolver<Aggregation, Object> aggregationParserResolver) {
        this.aggregationParserResolver.set(aggregationParserResolver);
        return this;
    }
}
