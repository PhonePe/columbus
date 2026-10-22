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
package com.phonepe.columbus.elastic7.storage.toolkit;

import com.phonepe.columbus.core.settings.RegistrySettings;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.core.utils.RegistryCreators;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.id.visitor.IdResolutionVisitor;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.index.visitor.IndexResolutionVisitor;
import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.dao.node.visitor.NodeIdResolutionVisitor;
import com.phonepe.columbus.elastic7.dao.id.ElasticId;
import com.phonepe.columbus.elastic7.dao.index.ElasticIndex;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.internal.dao.impl.ElasticIdResolver;
import com.phonepe.columbus.elastic7.internal.dao.impl.ElasticIndexResolver;
import com.phonepe.columbus.elastic7.internal.dao.impl.ElasticNodeIdResolver;
import com.phonepe.columbus.elastic7.internal.search.impl.ElasticAggregationParserResolver;
import com.phonepe.columbus.elastic7.internal.search.impl.ElasticAggregatorTransformer;
import com.phonepe.columbus.elastic7.internal.search.impl.ElasticPageResolver;
import com.phonepe.columbus.elastic7.internal.search.impl.ElasticQueryTransformer;
import com.phonepe.columbus.elastic7.internal.search.impl.ElasticScoreFunctionTransformer;
import com.phonepe.columbus.elastic7.internal.search.impl.ElasticSortTransformer;
import com.phonepe.columbus.elastic7.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.columbus.elastic7.search.page.ElasticPage;
import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.search.aggregation.visitor.AggregationParserResolutionVisitor;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.aggregator.visitor.AggregatorTransformationVisitor;
import com.phonepe.columbus.search.page.PageBounds;
import com.phonepe.columbus.search.page.visitor.PageResolutionVisitor;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.visitor.QueryTransformationVisitor;
import com.phonepe.columbus.search.score.ScoreFunction;
import com.phonepe.columbus.search.score.visitor.ScoreFunctionTransformationVisitor;
import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.search.sort.visitor.SortTransformationVisitor;
import com.phonepe.columbus.storage.settings.StorageSettings;
import com.phonepe.columbus.visitor.VisitorHelpers;
import com.phonepe.columbus.visitor.factory.VisitorRegistryFactory;
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
                        Generics.<IdResolutionVisitor<ElasticId, Id, Object, ElasticIdResolver>>typed(
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
                        Generics.<IndexResolutionVisitor<ElasticIndex, Index, Object, ElasticIndexResolver>>typed(
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
                        Generics.<NodeIdResolutionVisitor<ElasticNode, NodeId, Object, ElasticNodeIdResolver>>typed(
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
                        Generics.<QueryTransformationVisitor<QueryBuilder, Query, Object, ElasticQueryTransformer>>typed(
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
                        Generics.<SortTransformationVisitor<SortBuilder<?>, Sort, Object, ElasticSortTransformer>>typed(
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
                        Generics.<ScoreFunctionTransformationVisitor<FilterFunctionBuilder, ScoreFunction, Object,
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
                        Generics.<AggregatorTransformationVisitor<AggregationBuilder, Aggregator, Object,
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
                        Generics.<AggregationParserResolutionVisitor<Aggregation, Aggregator, Object,
                                ElasticAggregationParserResolver>>typed(AggregationParserResolutionVisitor.class),
                        ElasticAggregationParserResolver.class,
                        Aggregator.class,
                        Aggregator::getType,
                        overrides)))
                .toolkit(toolkit)
                .build();
    }

    public ElasticPageResolver elasticPageResolver(
            final VisitorRegistryFactory visitorFactory,
            @Nullable final RegistrySettings overrides
    ) {
        return ElasticPageResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        visitorFactory,
                        Generics.<PageResolutionVisitor<ElasticPage, PageBounds, Object, ElasticPageResolver>>typed(
                                PageResolutionVisitor.class),
                        ElasticPageResolver.class,
                        PageBounds.class,
                        PageBounds::getType,
                        overrides)))
                .build();
    }
}
