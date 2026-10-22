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
package com.phonepe.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.query.Collapse;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.sort.Sort;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(StandardRetriever.TYPE)
public class StandardRetriever implements Retriever {

    public static final String TYPE = "STANDARD";

    @JsonPropertyDescription("Main query to retrieve documents matching the criteria")
    private Query query;

    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    @JsonPropertyDescription("List of filter clauses to further refine the search results")
    private List<@NonNull Query> filterClauses;

    @JsonPropertyDescription("Sorting criteria for ordering the search results")
    private Sort sort;

    @JsonPropertyDescription("Minimum score threshold for documents to be included in the results")
    private Float minScore;

    @JsonPropertyDescription("Collapse configuration to group search results based on field values")
    private Collapse collapse;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
