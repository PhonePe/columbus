package com.phonepe.platform.columbus.elastic.internal.storage.toolkit;

import com.phonepe.platform.columbus.core.settings.RegistrySettings;
import com.phonepe.platform.columbus.dao.id.IdResolver;
import com.phonepe.platform.columbus.dao.index.IndexResolver;
import com.phonepe.platform.columbus.dao.node.NodeIdResolver;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.search.page.ElasticPage;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.configuration.IndexConfiguration;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkitHelpers;
import com.phonepe.platform.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.platform.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.platform.columbus.search.page.PageResolver;
import com.phonepe.platform.columbus.search.query.QueryTransformer;
import com.phonepe.platform.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.platform.columbus.search.sort.SortTransformer;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.Optional;

/**
 * The default behaviour for creating the toolkit.
 */
public class DefaultElasticToolkitInitializer extends LazyElasticToolkitInitializer {

    @Override
    protected boolean canInitializeIdResolver() {
        return getToolkit().isPresent() && getStorageSettings().isPresent();
    }

    @Override
    protected IdResolver<ElasticId> initializeIdResolver() {
        return ElasticToolkitHelpers.elasticIdResolver(getVisitorFactory(), getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeIndexResolver() {
        return getToolkit().isPresent()
                && getStorageSettings().isPresent()
                && getConfiguration().isPresent();
    }

    @Override
    protected IndexResolver<ElasticIndex> initializeIndexResolver() {
        final var indices = Optional.ofNullable(getConfiguration().get())
                .map(ElasticConfiguration::getIndices)
                .map(IndexConfiguration::getRegistry)
                .orElse(null);
        return ElasticToolkitHelpers.elasticIndexResolver(getVisitorFactory(), indices, getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeNodeIdResolver() {
        return getToolkit().isPresent() && getStorageSettings().isPresent();
    }

    @Override
    protected NodeIdResolver<ElasticNode> initializeNodeIdResolver() {
        return ElasticToolkitHelpers.elasticNodeIdResolver(getVisitorFactory(), getVisitorOverrides());
    }

    @Override
    protected boolean canInitializePageResolver() {
        return getToolkit().isPresent() && getStorageSettings().isPresent();
    }

    @Override
    protected PageResolver<ElasticPage> initializePageResolver() {
        return ElasticToolkitHelpers.elasticPageResolver(getVisitorFactory(), getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeToolkitData() {
        return getToolkit().isPresent()
                && getIdResolver().isPresent()
                && getIndexResolver().isPresent()
                && getNodeIdResolver().isPresent()
                && getPageResolver().isPresent();
    }

    @Override
    protected ElasticToolkitData initializeToolkitData() {
        return ElasticToolkitData.builder()
                .serDe(getToolkit().get().serDe())
                .idResolver(getIdResolver().get())
                .indexResolver(getIndexResolver().get())
                .nodeIdResolver(getNodeIdResolver().get())
                .pageResolver(getPageResolver().get())
                .build();
    }

    @Override
    protected boolean canInitializeQueryTransformer() {
        return getToolkit().isPresent() && getToolkitData().isPresent();
    }

    @Override
    protected QueryTransformer<QueryBuilder> initializeQueryTransformer() {
        return ElasticToolkitHelpers.elasticQueryTransformer(
                getVisitorFactory(),
                getToolkitData().get(),
                getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeSortTransformer() {
        return getToolkit().isPresent() && getToolkitData().isPresent();
    }

    @Override
    protected SortTransformer<SortBuilder<?>> initializeSortTransformer() {
        return ElasticToolkitHelpers.elasticSortTransformer(
                getVisitorFactory(),
                getToolkitData().get(),
                getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeScoreFunctionTransformer() {
        return getToolkit().isPresent() && getToolkitData().isPresent();
    }

    @Override
    protected ScoreFunctionTransformer<FilterFunctionBuilder> initializeScoreFunctionTransformer() {
        return ElasticToolkitHelpers.elasticScoreFunctionTransformer(
                getVisitorFactory(),
                getToolkitData().get(),
                getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeAggregatorTransformer() {
        return getToolkit().isPresent() && getToolkitData().isPresent();
    }

    @Override
    protected AggregatorTransformer<AggregationBuilder> initializeAggregatorTransformer() {
        return ElasticToolkitHelpers.elasticAggregatorTransformer(
                getVisitorFactory(),
                getToolkitData().get(),
                getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeAggregationParserResolver() {
        return getToolkit().isPresent() && getToolkitData().isPresent();
    }

    @Override
    protected AggregationParserResolver<Aggregation> initializeAggregationParserResolver() {
        return ElasticToolkitHelpers.elasticAggregationParserResolver(
                getVisitorFactory(),
                getToolkitData().get(),
                getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeElasticToolkit() {
        return getToolkitData().isPresent()
                && getQueryTransformer().isPresent()
                && getSortTransformer().isPresent()
                && getScoreFunctionTransformer().isPresent()
                && getAggregatorTransformer().isPresent()
                && getAggregationParserResolver().isPresent();
    }

    @Override
    protected ElasticToolkit initializeElasticToolkit() {
        return getToolkitData().get()
                .queryTransformer(getQueryTransformer().get())
                .sortTransformer(getSortTransformer().get())
                .scoreFunctionTransformer(getScoreFunctionTransformer().get())
                .aggregatorTransformer(getAggregatorTransformer().get())
                .aggregationParserResolver(getAggregationParserResolver().get());
    }

    private VisitorRegistryFactory getVisitorFactory() {
        return getToolkit().get().visitorFactory();
    }

    private RegistrySettings getVisitorOverrides() {
        return getStorageSettings().get().getVisitorOverrides();
    }
}
