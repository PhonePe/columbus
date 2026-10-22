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
package com.phonepe.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.columbus.search.aggregator.AbstractAggregator;
import com.phonepe.columbus.search.aggregator.Aggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@JsonClassDescription("Abstract base class for bucket aggregators that group documents and support nested sub-aggregations")
@Data
@SuperBuilder(toBuilder = true)
public abstract class BucketAggregator extends AbstractAggregator {

    @JsonPropertyDescription("List of nested aggregators to execute within each bucket")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Aggregator> subAggregators;
}
