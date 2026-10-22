/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic7.internal.storage.toolkit;

import com.phonepe.columbus.common.initializer.Initializer;
import com.phonepe.columbus.common.initializer.LateInit;
import com.phonepe.columbus.common.initializer.LazyInit;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.dao.id.IdResolver;
import com.phonepe.columbus.dao.index.IndexResolver;
import com.phonepe.columbus.dao.node.NodeIdResolver;
import com.phonepe.columbus.elastic7.dao.id.ElasticId;
import com.phonepe.columbus.elastic7.dao.index.ElasticIndex;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.search.page.ElasticPage;
import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.columbus.search.page.PageResolver;
import com.phonepe.columbus.search.query.QueryTransformer;
import com.phonepe.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.columbus.search.sort.SortTransformer;
import com.phonepe.columbus.storage.settings.StorageSettings;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

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

    private final LazyInit<QueryTransformer<QueryBuilder, Object>> queryTransformer =
            new LazyInit<>("queryTransformer", this::canInitializeQueryTransformer, this::initializeQueryTransformer);
    private final LazyInit<SortTransformer<SortBuilder<?>, Object>> sortTransformer =
            new LazyInit<>("sortTransformer", this::canInitializeSortTransformer, this::initializeSortTransformer);
    private final LazyInit<ScoreFunctionTransformer<FilterFunctionBuilder, Object>> scoreFunctionTransformer =
            new LazyInit<>("scoreFunctionTransformer", this::canInitializeScoreFunctionTransformer,
                    this::initializeScoreFunctionTransformer);
    private final LazyInit<AggregatorTransformer<AggregationBuilder, Object>> aggregatorTransformer =
            new LazyInit<>("aggregatorTransformer", this::canInitializeAggregatorTransformer,
                    this::initializeAggregatorTransformer);
    private final LazyInit<AggregationParserResolver<Aggregation, Object>> aggregationParserResolver =
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

    public Initializer<QueryTransformer<QueryBuilder, Object>> getQueryTransformer() {
        return queryTransformer;
    }

    @SuppressWarnings("java:S1452")
    public Initializer<SortTransformer<SortBuilder<?>, Object>> getSortTransformer() {
        return sortTransformer;
    }

    public Initializer<ScoreFunctionTransformer<FilterFunctionBuilder, Object>> getScoreFunctionTransformer() {
        return scoreFunctionTransformer;
    }

    public Initializer<AggregatorTransformer<AggregationBuilder, Object>> getAggregatorTransformer() {
        return aggregatorTransformer;
    }

    public Initializer<AggregationParserResolver<Aggregation, Object>> getAggregationParserResolver() {
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

    protected abstract QueryTransformer<QueryBuilder, Object> initializeQueryTransformer();

    protected abstract boolean canInitializeSortTransformer();

    @SuppressWarnings("java:S1452")
    protected abstract SortTransformer<SortBuilder<?>, Object> initializeSortTransformer();

    protected abstract boolean canInitializeScoreFunctionTransformer();

    protected abstract ScoreFunctionTransformer<FilterFunctionBuilder, Object> initializeScoreFunctionTransformer();

    protected abstract boolean canInitializeAggregatorTransformer();

    protected abstract AggregatorTransformer<AggregationBuilder, Object> initializeAggregatorTransformer();

    protected abstract boolean canInitializeAggregationParserResolver();

    protected abstract AggregationParserResolver<Aggregation, Object> initializeAggregationParserResolver();

    protected abstract boolean canInitializeElasticToolkit();

    protected abstract ElasticToolkit initializeElasticToolkit();
}
