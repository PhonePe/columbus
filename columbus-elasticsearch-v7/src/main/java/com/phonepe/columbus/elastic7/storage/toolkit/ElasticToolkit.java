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

import com.phonepe.columbus.dao.id.IdResolver;
import com.phonepe.columbus.dao.index.IndexResolver;
import com.phonepe.columbus.dao.node.NodeIdResolver;
import com.phonepe.columbus.elastic7.dao.id.ElasticId;
import com.phonepe.columbus.elastic7.dao.index.ElasticIndex;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.search.page.ElasticPage;
import com.phonepe.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.columbus.search.page.PageResolver;
import com.phonepe.columbus.search.query.QueryTransformer;
import com.phonepe.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.columbus.search.sort.SortTransformer;
import com.phonepe.columbus.serde.SerDe;
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
