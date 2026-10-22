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
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Defines which fields to fetch in the result.")
@Data
@Builder
@Jacksonized
public class Projection {

    @JsonPropertyDescription("Whether to disable the projection and fetch all fields.")
    @Accessors(fluent = true)
    private boolean disabled;

    @JsonPropertyDescription("List of fields to include in the result.")
    @Singular(ignoreNullCollections = true)
    private List<String> includeFields;

    @JsonPropertyDescription("List of fields to exclude from the result.")
    @Singular(ignoreNullCollections = true)
    private List<String> excludeFields;
}
