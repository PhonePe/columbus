package com.phonepe.columbus.elastic9.storage.toolkit;

import co.elastic.clients.elasticsearch._types.Retriever;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.dao.id.IdResolver;
import com.phonepe.columbus.dao.index.IndexResolver;
import com.phonepe.columbus.dao.node.NodeIdResolver;
import com.phonepe.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.columbus.elastic9.dao.index.ElasticIndex;
import com.phonepe.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.columbus.elastic9.search.page.ElasticPage;
import com.phonepe.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.columbus.search.page.PageResolver;
import com.phonepe.columbus.search.query.QueryTransformer;
import com.phonepe.columbus.search.retriever.RetrieverTransformer;
import com.phonepe.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.columbus.search.sort.SortTransformer;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.type.TypeResolver;

/**
 * A collection of tools to transform data between Columbus and ElasticSearch.
 */
public interface ElasticToolkit {

    SerDe serDe();

    TypeResolver typeResolver();

    IdResolver<ElasticId, Object> idResolver();

    IndexResolver<ElasticIndex, Object> indexResolver();

    NodeIdResolver<ElasticNode, Object> nodeIdResolver();

    QueryTransformer<QueryVariant, Object> queryTransformer();

    RetrieverTransformer<Retriever, Object> retrieverTransformer();

    SortTransformer<SortOptions, Object> sortTransformer();

    ScoreFunctionTransformer<FunctionScore, Object> scoreFunctionTransformer();

    AggregatorTransformer<Aggregation, Object> aggregatorTransformer();

    AggregationParserResolver<Aggregate, Object> aggregationParserResolver();

    PageResolver<ElasticPage, Object> pageResolver();
}
