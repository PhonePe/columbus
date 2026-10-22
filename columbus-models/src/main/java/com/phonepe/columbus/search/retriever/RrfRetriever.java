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
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Retriever that combines results from multiple retrievers using Reciprocal Rank Fusion (RRF) technique.")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RrfRetriever.TYPE)
public class RrfRetriever implements Retriever {

    public static final String TYPE = "RRF";

    @JsonPropertyDescription("List of retrievers whose results will be combined using Reciprocal Rank Fusion")
    @Singular(value = "retriever", ignoreNullCollections = true)
    private List<Retriever> retrievers;

    @JsonPropertyDescription("List of filter queries to be applied to the combined results")
    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    @JsonPropertyDescription("Constant value used in the RRF scoring formula")
    private Integer rankConstant;

    @JsonPropertyDescription("Window size for considering top results from each retriever")
    private Integer rankWindowSize;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
