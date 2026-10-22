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
package com.phonepe.columbus.elastic9.search.aggregator;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.aggregator.visitor.AggregatorTransformationVisitor;

/**
 * An interface to transform a specific {@link Aggregator} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Aggregator} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface AggregatorToElastic<T extends Aggregator, C extends ElasticAggregatorTransformationContext<C>>
        extends AggregatorTransformationVisitor<Aggregation, T, Object, C> {
}
