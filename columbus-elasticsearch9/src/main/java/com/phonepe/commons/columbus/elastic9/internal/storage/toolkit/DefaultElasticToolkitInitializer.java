package com.phonepe.commons.columbus.elastic9.internal.storage.toolkit;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.core.settings.RegistrySettings;
import com.phonepe.commons.columbus.dao.id.IdResolver;
import com.phonepe.commons.columbus.dao.index.IndexResolver;
import com.phonepe.commons.columbus.dao.node.NodeIdResolver;
import com.phonepe.commons.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.commons.columbus.elastic9.dao.index.ElasticIndex;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic9.search.page.ElasticPage;
import com.phonepe.commons.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.configuration.IndexConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkitHelpers;
import com.phonepe.commons.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.commons.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.commons.columbus.search.page.PageResolver;
import com.phonepe.commons.columbus.search.query.QueryTransformer;
import com.phonepe.commons.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.commons.columbus.search.sort.SortTransformer;
import com.phonepe.commons.columbus.visitor.factory.VisitorRegistryFactory;

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
    protected IdResolver<ElasticId, Object> initializeIdResolver() {
        return ElasticToolkitHelpers.elasticIdResolver(getVisitorFactory(), getVisitorOverrides());
    }

    @Override
    protected boolean canInitializeIndexResolver() {
        return getToolkit().isPresent()
                && getStorageSettings().isPresent()
                && getConfiguration().isPresent();
    }

    @Override
    protected IndexResolver<ElasticIndex, Object> initializeIndexResolver() {
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
    protected NodeIdResolver<ElasticNode, Object> initializeNodeIdResolver() {
        return ElasticToolkitHelpers.elasticNodeIdResolver(getVisitorFactory(), getVisitorOverrides());
    }

    @Override
    protected boolean canInitializePageResolver() {
        return getToolkit().isPresent() && getStorageSettings().isPresent();
    }

    @Override
    protected PageResolver<ElasticPage, Object> initializePageResolver() {
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
                .typeResolver(getToolkit().get().typeResolver())
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
    protected QueryTransformer<QueryVariant, Object> initializeQueryTransformer() {
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
    protected SortTransformer<SortOptions, Object> initializeSortTransformer() {
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
    protected ScoreFunctionTransformer<FunctionScore, Object> initializeScoreFunctionTransformer() {
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
    protected AggregatorTransformer<Aggregation, Object> initializeAggregatorTransformer() {
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
    protected AggregationParserResolver<Aggregate, Object> initializeAggregationParserResolver() {
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
