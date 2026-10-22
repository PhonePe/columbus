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
package com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.CardinalityAggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.columbus.search.aggregation.metrics.CardinalityAggregation;

public class CardinalityAggregationParser implements ElasticAggregationParser {

    @Override
    public CardinalityAggregation parse(final Aggregate result, final String name) {
        if (result.isCardinality()) {
            return parseCardinality(result.cardinality(), name);
        }
        throw new IllegalArgumentException("Expected CardinalityAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private CardinalityAggregation parseCardinality(final CardinalityAggregate cardinality, final String name) {
        return CardinalityAggregation.builder()
                .name(name)
                .count(cardinality.value())
                .build();
    }
}
