package com.phonepe.commons.columbus.elastic9.internal.storage.toolkit;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.common.initializer.Initializer;
import com.phonepe.commons.columbus.common.initializer.LateInit;
import com.phonepe.commons.columbus.common.initializer.LazyInit;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.dao.id.IdResolver;
import com.phonepe.commons.columbus.dao.index.IndexResolver;
import com.phonepe.commons.columbus.dao.node.NodeIdResolver;
import com.phonepe.commons.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.commons.columbus.elastic9.dao.index.ElasticIndex;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic9.search.page.ElasticPage;
import com.phonepe.commons.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.commons.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.commons.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.commons.columbus.search.page.PageResolver;
import com.phonepe.commons.columbus.search.query.QueryTransformer;
import com.phonepe.commons.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.commons.columbus.search.sort.SortTransformer;
import com.phonepe.commons.columbus.storage.settings.StorageSettings;

/**
 * An object that uses the different {@link Initializer}s to create a dependency graph between the fields of
 * {@link ElasticToolkit}. The preconditions and initializers are exposed to allow the children to define the
 * behaviour for each of the fields independently.
 *
 * @see DefaultElasticToolkitInitializer
 */
public abstract class LazyElasticToolkitInitializer implements ElasticToolkitInitializer {

    private final LateInit<Toolkit> toolkit = new LateInit<>("toolkit");
    private final LateInit<StorageSettings> storageSettings = new LateInit<>("storageSettings");
    private final LateInit<ElasticConfiguration> configuration = new LateInit<>("configuration");

    private final LazyInit<IdResolver<ElasticId, Object>> idResolver =
            new LazyInit<>("idResolver", this::canInitializeIdResolver, this::initializeIdResolver);
    private final LazyInit<IndexResolver<ElasticIndex, Object>> indexResolver =
            new LazyInit<>("indexResolver", this::canInitializeIndexResolver, this::initializeIndexResolver);
    private final LazyInit<NodeIdResolver<ElasticNode, Object>> nodeIdResolver =
            new LazyInit<>("nodeIdResolver", this::canInitializeNodeIdResolver, this::initializeNodeIdResolver);
    private final LazyInit<PageResolver<ElasticPage, Object>> pageResolver =
            new LazyInit<>("pageResolver", this::canInitializePageResolver, this::initializePageResolver);

    private final LazyInit<ElasticToolkitData> toolkitData =
            new LazyInit<>("toolkitData", this::canInitializeToolkitData, this::initializeToolkitData);

    private final LazyInit<QueryTransformer<QueryVariant, Object>> queryTransformer =
            new LazyInit<>("queryTransformer", this::canInitializeQueryTransformer, this::initializeQueryTransformer);
    private final LazyInit<SortTransformer<SortOptions, Object>> sortTransformer =
            new LazyInit<>("sortTransformer", this::canInitializeSortTransformer, this::initializeSortTransformer);
    private final LazyInit<ScoreFunctionTransformer<FunctionScore, Object>> scoreFunctionTransformer =
            new LazyInit<>("scoreFunctionTransformer", this::canInitializeScoreFunctionTransformer,
                    this::initializeScoreFunctionTransformer);
    private final LazyInit<AggregatorTransformer<Aggregation, Object>> aggregatorTransformer =
            new LazyInit<>("aggregatorTransformer", this::canInitializeAggregatorTransformer,
                    this::initializeAggregatorTransformer);
    private final LazyInit<AggregationParserResolver<Aggregate, Object>> aggregationParserResolver =
            new LazyInit<>("aggregationParserResolver", this::canInitializeAggregationParserResolver,
                    this::initializeAggregationParserResolver);
    private final LazyInit<ElasticToolkit> elasticToolkit =
            new LazyInit<>("elasticToolkit", this::canInitializeElasticToolkit, this::initializeElasticToolkit);

    @Override
    public void use(final Toolkit toolkit) {
        this.toolkit.set(toolkit);
    }

    @Override
    public void use(final StorageSettings storageSettings) {
        this.storageSettings.set(storageSettings);
    }

    @Override
    public void use(final ElasticConfiguration configuration) {
        this.configuration.set(configuration);
    }

    @Override
    public ElasticToolkit get() {
        return getElasticToolkit().get();
    }

    public Initializer<Toolkit> getToolkit() {
        return toolkit;
    }

    public Initializer<StorageSettings> getStorageSettings() {
        return storageSettings;
    }

    public Initializer<ElasticConfiguration> getConfiguration() {
        return configuration;
    }

    public Initializer<IdResolver<ElasticId, Object>> getIdResolver() {
        return idResolver;
    }

    public Initializer<IndexResolver<ElasticIndex, Object>> getIndexResolver() {
        return indexResolver;
    }

    public Initializer<NodeIdResolver<ElasticNode, Object>> getNodeIdResolver() {
        return nodeIdResolver;
    }

    public Initializer<PageResolver<ElasticPage, Object>> getPageResolver() {
        return pageResolver;
    }

    public Initializer<ElasticToolkitData> getToolkitData() {
        return toolkitData;
    }

    public Initializer<QueryTransformer<QueryVariant, Object>> getQueryTransformer() {
        return queryTransformer;
    }

    public Initializer<SortTransformer<SortOptions, Object>> getSortTransformer() {
        return sortTransformer;
    }

    public Initializer<ScoreFunctionTransformer<FunctionScore, Object>> getScoreFunctionTransformer() {
        return scoreFunctionTransformer;
    }

    public Initializer<AggregatorTransformer<Aggregation, Object>> getAggregatorTransformer() {
        return aggregatorTransformer;
    }

    public Initializer<AggregationParserResolver<Aggregate, Object>> getAggregationParserResolver() {
        return aggregationParserResolver;
    }

    public Initializer<ElasticToolkit> getElasticToolkit() {
        return elasticToolkit;
    }

    protected abstract boolean canInitializeIdResolver();

    protected abstract IdResolver<ElasticId, Object> initializeIdResolver();

    protected abstract boolean canInitializeIndexResolver();

    protected abstract IndexResolver<ElasticIndex, Object> initializeIndexResolver();

    protected abstract boolean canInitializeNodeIdResolver();

    protected abstract NodeIdResolver<ElasticNode, Object> initializeNodeIdResolver();

    protected abstract boolean canInitializePageResolver();

    protected abstract PageResolver<ElasticPage, Object> initializePageResolver();

    protected abstract boolean canInitializeToolkitData();

    protected abstract ElasticToolkitData initializeToolkitData();

    protected abstract boolean canInitializeQueryTransformer();

    protected abstract QueryTransformer<QueryVariant, Object> initializeQueryTransformer();

    protected abstract boolean canInitializeSortTransformer();

    protected abstract SortTransformer<SortOptions, Object> initializeSortTransformer();

    protected abstract boolean canInitializeScoreFunctionTransformer();

    protected abstract ScoreFunctionTransformer<FunctionScore, Object> initializeScoreFunctionTransformer();

    protected abstract boolean canInitializeAggregatorTransformer();

    protected abstract AggregatorTransformer<Aggregation, Object> initializeAggregatorTransformer();

    protected abstract boolean canInitializeAggregationParserResolver();

    protected abstract AggregationParserResolver<Aggregate, Object> initializeAggregationParserResolver();

    protected abstract boolean canInitializeElasticToolkit();

    protected abstract ElasticToolkit initializeElasticToolkit();
}
