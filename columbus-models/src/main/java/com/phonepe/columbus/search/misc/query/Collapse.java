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
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Selects the top result for each group formed based on the value in the field.")
@Data
@Builder
@Jacksonized
public class Collapse {

    @JsonPropertyDescription("The field on which to collapse the results")
    private String field;

    @JsonPropertyDescription("A set of queries to be run on each group")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull InnerQuery<?>> innerQueries;

    @JsonPropertyDescription("The number of groups that can be queried in parallel at a time for inner queries")
    private Integer parallelism;
}
