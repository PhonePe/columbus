package com.phonepe.platform.columbus.elastic.storage.toolkit;

import com.phonepe.platform.columbus.dao.id.IdResolver;
import com.phonepe.platform.columbus.dao.index.IndexResolver;
import com.phonepe.platform.columbus.dao.node.NodeIdResolver;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.dao.index.ElasticIndex;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.search.page.ElasticPage;
import com.phonepe.platform.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.platform.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.platform.columbus.search.page.PageResolver;
import com.phonepe.platform.columbus.search.query.QueryTransformer;
import com.phonepe.platform.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.platform.columbus.search.sort.SortTransformer;
import com.phonepe.platform.columbus.serde.SerDe;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 * A collection of tools to transform data between Columbus and ElasticSearch.
 */
public interface ElasticToolkit {

    SerDe serDe();

    IdResolver<ElasticId, Object> idResolver();

    IndexResolver<ElasticIndex, Object> indexResolver();

    NodeIdResolver<ElasticNode, Object> nodeIdResolver();

    QueryTransformer<QueryBuilder, Object> queryTransformer();

    @SuppressWarnings("java:S1452")
    SortTransformer<SortBuilder<?>, Object> sortTransformer();

    ScoreFunctionTransformer<FilterFunctionBuilder, Object> scoreFunctionTransformer();

    AggregatorTransformer<AggregationBuilder, Object> aggregatorTransformer();

    AggregationParserResolver<Aggregation, Object> aggregationParserResolver();

    PageResolver<ElasticPage, Object> pageResolver();
}
