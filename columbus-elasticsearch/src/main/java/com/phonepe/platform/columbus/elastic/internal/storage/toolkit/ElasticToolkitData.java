package com.phonepe.platform.columbus.elastic.internal.storage.toolkit;

import com.phonepe.platform.columbus.common.initializer.LateInit;
import com.phonepe.platform.columbus.dao.id.IdResolver;
import com.phonepe.platform.columbus.dao.index.IndexResolver;
import com.phonepe.platform.columbus.dao.node.NodeIdResolver;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.search.page.ElasticPage;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.platform.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.platform.columbus.search.page.PageResolver;
import com.phonepe.platform.columbus.search.query.QueryTransformer;
import com.phonepe.platform.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.platform.columbus.search.sort.SortTransformer;
import com.phonepe.platform.columbus.serde.SerDe;
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
    private final IdResolver<ElasticId> idResolver;

    @Getter
    @NonNull
    private final IndexResolver<ElasticIndex> indexResolver;

    @Getter
    @NonNull
    private final NodeIdResolver<ElasticNode> nodeIdResolver;

    @Getter
    @NonNull
    private final PageResolver<ElasticPage> pageResolver;

    private final LateInit<QueryTransformer<QueryBuilder>> queryTransformer = new LateInit<>("Query");

    private final LateInit<SortTransformer<SortBuilder<?>>> sortTransformer = new LateInit<>("Sort");

    private final LateInit<ScoreFunctionTransformer<FilterFunctionBuilder>> scoreFunctionTransformer =
            new LateInit<>("ScoreFunction");

    private final LateInit<AggregatorTransformer<AggregationBuilder>> aggregatorTransformer =
            new LateInit<>("Aggregator");

    private final LateInit<AggregationParserResolver<Aggregation>> aggregationParserResolver =
            new LateInit<>("AggregationParser");

    @Override
    public QueryTransformer<QueryBuilder> queryTransformer() {
        return queryTransformer.get();
    }

    @Override
    public SortTransformer<SortBuilder<?>> sortTransformer() {
        return sortTransformer.get();
    }

    @Override
    public ScoreFunctionTransformer<FilterFunctionBuilder> scoreFunctionTransformer() {
        return scoreFunctionTransformer.get();
    }

    @Override
    public AggregatorTransformer<AggregationBuilder> aggregatorTransformer() {
        return aggregatorTransformer.get();
    }

    @Override
    public AggregationParserResolver<Aggregation> aggregationParserResolver() {
        return aggregationParserResolver.get();
    }

    public ElasticToolkitData queryTransformer(final QueryTransformer<QueryBuilder> queryTransformer) {
        this.queryTransformer.set(queryTransformer);
        return this;
    }

    public ElasticToolkitData sortTransformer(final SortTransformer<SortBuilder<?>> sortTransformer) {
        this.sortTransformer.set(sortTransformer);
        return this;
    }

    public ElasticToolkitData scoreFunctionTransformer(final ScoreFunctionTransformer<FilterFunctionBuilder> scoreFunctionTransformer) {
        this.scoreFunctionTransformer.set(scoreFunctionTransformer);
        return this;
    }

    public ElasticToolkitData aggregatorTransformer(final AggregatorTransformer<AggregationBuilder> aggregatorTransformer) {
        this.aggregatorTransformer.set(aggregatorTransformer);
        return this;
    }

    public ElasticToolkitData aggregationParserResolver(final AggregationParserResolver<Aggregation> aggregationParserResolver) {
        this.aggregationParserResolver.set(aggregationParserResolver);
        return this;
    }
}
