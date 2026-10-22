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
package com.phonepe.columbus.search.aggregator.metrics;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.aggregator.AbstractAggregator;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Metrics aggregator that returns the top matching documents within each bucket")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TopHitsAggregator.TYPE)
public class TopHitsAggregator<T> extends AbstractAggregator {

    public static final String TYPE = "TOP_HITS";

    @JsonPropertyDescription("The starting offset for pagination (default: 0)")
    @Builder.Default
    private int from = 0;

    @JsonPropertyDescription("Maximum number of top documents to return (default: 3)")
    @Builder.Default
    private int size = 3;

    @JsonPropertyDescription("List of sort criteria to determine the top documents")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Sort> sorts;

    @JsonPropertyDescription("Field projection to control which fields are returned in the documents")
    private Projection projection;

    @JsonPropertyDescription("Entity type for deserialization of returned documents")
    private EntityType<T> entityType;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
