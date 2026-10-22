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
package com.phonepe.columbus.elastic7.internal.search.aggregation.parser.bucket;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Getter(AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class BucketAggregationParser implements ElasticAggregationParser {

    @Singular(ignoreNullCollections = true)
    private final Map<String, AggregationParser<Aggregation>> subAggregationParsers;

    protected Stream<com.phonepe.columbus.search.aggregation.Aggregation> parseSubAggregations(
            final List<Aggregation> aggregations
    ) {
        return aggregations.stream()
                .map(aggregation -> subAggregationParsers.get(aggregation.getName()).parse(aggregation));
    }
}
