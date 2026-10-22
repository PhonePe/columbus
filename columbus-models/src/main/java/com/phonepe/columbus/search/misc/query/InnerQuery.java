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
package com.phonepe.columbus.search.misc.query;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents a query in a collapsed group.
 *
 * @param <T> the type of the result entities.
 */
@JsonClassDescription("Represents a query in a collapsed group.")
@Data
@Builder
@Jacksonized
public class InnerQuery<T> {

    @JsonPropertyDescription("Name of the inner query")
    @NonNull
    private final String name;

    @JsonPropertyDescription("Starting index of the results to be fetched")
    @Builder.Default
    private int from = 0;

    @JsonPropertyDescription("Number of results to be fetched")
    @Builder.Default
    private int size = 3;

    @JsonPropertyDescription("Sorting criteria for ordering the search results within the collapsed group")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Sort> sorts;

    @JsonPropertyDescription("Projection specifying which fields to include in the results")
    private Projection projection;

    @JsonPropertyDescription("Collapse configuration for the inner query")
    private Collapse innerCollapse;

    @JsonPropertyDescription("Type of the entities being queried")
    @NonNull
    private EntityType<T> entityType;

    @JsonPropertyDescription("Flag to ignore invalid inner queries")
    private Boolean ignoreInvalid;

    public static <T> InnerQueryBuilder<T> forType(final EntityType<T> type) {
        return InnerQuery.<T>builder()
                .entityType(type);
    }
}
