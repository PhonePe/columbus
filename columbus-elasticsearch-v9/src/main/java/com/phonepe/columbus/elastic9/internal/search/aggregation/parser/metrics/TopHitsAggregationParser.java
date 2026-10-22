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
import co.elastic.clients.elasticsearch._types.aggregations.TopHitsAggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.search.aggregation.metrics.TopHitsAggregation;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.type.EntityType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class TopHitsAggregationParser<T> implements ElasticAggregationParser {

    @NonNull
    private final EntityType<T> entityType;
    @NonNull
    private final SerDe serDe;

    @Override
    public TopHitsAggregation<T> parse(final Aggregate result, final String name) {
        if (result.isTopHits()) {
            return parseStats(result.topHits(), name);
        }
        throw new IllegalArgumentException("Expected TopHitsAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private TopHitsAggregation<T> parseStats(final TopHitsAggregate topHits, final String name) {
        return TopHitsAggregation.<T>builder()
                .name(name)
                .topHits(MiscTransforms.fromElasticForInnerResult(topHits.hits(), entityType, serDe, null))
                .build();
    }
}
