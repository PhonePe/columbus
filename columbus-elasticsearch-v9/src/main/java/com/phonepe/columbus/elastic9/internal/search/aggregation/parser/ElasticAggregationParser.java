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
package com.phonepe.columbus.elastic9.internal.search.aggregation.parser;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.search.aggregation.Aggregation;
import com.phonepe.columbus.search.aggregation.AggregationParser;

/**
 * An {@link AggregationParser} to parse ElasticSearch's {@link Aggregate}s.
 */
public interface ElasticAggregationParser extends AggregationParser<Aggregate> {

    default Aggregation parse(final Aggregate result) {
        throw new IllegalArgumentException("ES9 aggregation parser requires aggregation name to parse");
    }
}
