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
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.columbus.search.aggregation.metrics.StatsAggregation;

public class StatsAggregationParser implements ElasticAggregationParser {

    @Override
    public StatsAggregation parse(final Aggregate result, final String name) {
        if (result.isStats()) {
            return parseStats(result.stats(), name);
        }
        throw new IllegalArgumentException("Expected StatsAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private StatsAggregation parseStats(final StatsAggregate stats, final String name) {
        return StatsAggregation.builder()
                .name(name)
                .count(stats.count())
                .minimum(stats.min())
                .maximum(stats.max())
                .sum(stats.sum())
                .average(stats.avg())
                .build();
    }
}
