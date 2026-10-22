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
package com.phonepe.columbus.elastic7.internal.search.aggregation.parser;

import com.phonepe.columbus.search.aggregation.AggregationParser;
import org.elasticsearch.search.aggregations.Aggregation;

/**
 * An {@link AggregationParser} to parse ElasticSearch's {@link Aggregation}s.
 */
public interface ElasticAggregationParser extends AggregationParser<Aggregation> {

    default com.phonepe.columbus.search.aggregation.Aggregation parse(
            final Aggregation result,
            final String name
    ) {
        // Delegating to parse(result) as name is already part of result in ES 7 Aggregation
        return parse(result);
    }
}
