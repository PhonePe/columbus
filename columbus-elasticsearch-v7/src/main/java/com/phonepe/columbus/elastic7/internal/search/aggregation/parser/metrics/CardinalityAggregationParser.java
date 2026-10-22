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
package com.phonepe.columbus.elastic7.internal.search.aggregation.parser.metrics;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.columbus.search.aggregation.metrics.CardinalityAggregation;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;

public class CardinalityAggregationParser implements ElasticAggregationParser {

    @Override
    public CardinalityAggregation parse(final Aggregation result) {
        if (result instanceof ParsedCardinality cardinality) {
            return parseCardinality(cardinality);
        }
        throw new IllegalArgumentException("Expected ParsedCardinality, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private CardinalityAggregation parseCardinality(final ParsedCardinality cardinality) {
        return CardinalityAggregation.builder()
                .name(cardinality.getName())
                .count(cardinality.getValue())
                .build();
    }
}
