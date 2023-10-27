package com.phonepe.platform.columbus.elastic.storage.toolkit;

import com.phonepe.platform.columbus.core.settings.RegistrySettings;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.dao.id.visitor.IdResolutionVisitor;
import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.dao.index.visitor.IndexResolutionVisitor;
import com.phonepe.platform.columbus.dao.node.NodeId;
import com.phonepe.platform.columbus.dao.node.visitor.NodeIdResolutionVisitor;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.internal.dao.impl.ElasticIdResolver;
import com.phonepe.platform.columbus.elastic.internal.dao.impl.ElasticIndexResolver;
import com.phonepe.platform.columbus.elastic.internal.dao.impl.ElasticNodeIdResolver;
import com.phonepe.platform.columbus.elastic.internal.search.impl.ElasticAggregationParserResolver;
import com.phonepe.platform.columbus.elastic.internal.search.impl.ElasticAggregatorTransformer;
import com.phonepe.platform.columbus.elastic.internal.search.impl.ElasticQueryTransformer;
import com.phonepe.platform.columbus.elastic.internal.search.impl.ElasticScoreFunctionTransformer;
import com.phonepe.platform.columbus.elastic.internal.search.impl.ElasticSortTransformer;
import com.phonepe.platform.columbus.elastic.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.search.aggregation.visitor.AggregationParserResolutionVisitor;
import com.phonepe.platform.columbus.search.aggregator.Aggregator;
import com.phonepe.platform.columbus.search.aggregator.visitor.AggregatorTransformationVisitor;
import com.phonepe.platform.columbus.search.query.Query;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationVisitor;
import com.phonepe.platform.columbus.search.score.ScoreFunction;
import com.phonepe.platform.columbus.search.score.visitor.ScoreFunctionTransformationVisitor;
import com.phonepe.platform.columbus.search.sort.Sort;
import com.phonepe.platform.columbus.search.sort.visitor.SortTransformationVisitor;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryFactory;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * A collection of helper methods to create different instances required to create an {@link ElasticToolkit}.
 */
@UtilityClass
public class ElasticToolkitHelpers {

    public ElasticToolkit defaultInstance(final Toolkit toolkit) {
        final var initializer = new DefaultElasticToolkitInitializer();
        initializer.use(toolkit);
        initializer.use(StorageSettings.defaults());
        initializer.use((ElasticConfiguration) null);
        return initializer.get();
    }

    public ElasticIdResolver elasticIdResolver(
            final VisitorRegistryFactory visitorFactory,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticIdResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<IdResolutionVisitor<ElasticId, Id, ElasticIdResolver>>typed(
                                IdResolutionVisitor.class),
                        ElasticIdResolver.class,
                        Id.class,
                        Id::getType,
                        overrides)))
                .build();
    }

    public ElasticIndexResolver elasticIndexResolver(
            final VisitorRegistryFactory visitorFactory,
            @Nullable final Map<String, String> indices,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticIndexResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<IndexResolutionVisitor<ElasticIndex, Index, ElasticIndexResolver>>typed(
                                IndexResolutionVisitor.class),
                        ElasticIndexResolver.class,
                        Index.class,
                        Index::getType,
                        overrides)))
                .indexRegistry(Registries.withValues(MapUtils.emptyIfNull(indices)))
                .build();
    }

    public ElasticNodeIdResolver elasticNodeIdResolver(
            final VisitorRegistryFactory visitorFactory,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticNodeIdResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<NodeIdResolutionVisitor<ElasticNode, NodeId, ElasticNodeIdResolver>>typed(
                                NodeIdResolutionVisitor.class),
                        ElasticNodeIdResolver.class,
                        NodeId.class,
                        NodeId::getType,
                        overrides)))
                .build();
    }

    public ElasticQueryTransformer elasticQueryTransformer(
            final VisitorRegistryFactory visitorFactory,
            final ElasticToolkit toolkit,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticQueryTransformer.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<QueryTransformationVisitor<QueryBuilder, Query, ElasticQueryTransformer>>typed(
                                QueryTransformationVisitor.class),
                        ElasticQueryTransformer.class,
                        Query.class,
                        Query::getType,
                        overrides)))
                .toolkit(toolkit)
                .build();
    }

    public ElasticSortTransformer elasticSortTransformer(
            final VisitorRegistryFactory visitorFactory,
            final ElasticToolkit toolkit,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticSortTransformer.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<SortTransformationVisitor<SortBuilder<?>, Sort, ElasticSortTransformer>>typed(
                                SortTransformationVisitor.class),
                        ElasticSortTransformer.class,
                        Sort.class,
                        Sort::getType,
                        overrides)))
                .toolkit(toolkit)
                .build();
    }

    public ElasticScoreFunctionTransformer elasticScoreFunctionTransformer(
            final VisitorRegistryFactory visitorFactory,
            final ElasticToolkit toolkit,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticScoreFunctionTransformer.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<ScoreFunctionTransformationVisitor<FilterFunctionBuilder, ScoreFunction,
                                ElasticScoreFunctionTransformer>>typed(ScoreFunctionTransformationVisitor.class),
                        ElasticScoreFunctionTransformer.class,
                        ScoreFunction.class,
                        ScoreFunction::getType,
                        overrides)))
                .toolkit(toolkit)
                .build();
    }

    public ElasticAggregatorTransformer elasticAggregatorTransformer(
            final VisitorRegistryFactory visitorFactory,
            final ElasticToolkit toolkit,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticAggregatorTransformer.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<AggregatorTransformationVisitor<AggregationBuilder, Aggregator,
                                ElasticAggregatorTransformer>>typed(AggregatorTransformationVisitor.class),
                        ElasticAggregatorTransformer.class,
                        Aggregator.class,
                        Aggregator::getType,
                        overrides)))
                .toolkit(toolkit)
                .build();
    }

    public ElasticAggregationParserResolver elasticAggregationParserResolver(
            final VisitorRegistryFactory visitorFactory,
            final ElasticToolkit toolkit,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticAggregationParserResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<AggregationParserResolutionVisitor<Aggregation, Aggregator,
                                ElasticAggregationParserResolver>>typed(AggregationParserResolutionVisitor.class),
                        ElasticAggregationParserResolver.class,
                        Aggregator.class,
                        Aggregator::getType,
                        overrides)))
                .toolkit(toolkit)
                .build();
    }
}
