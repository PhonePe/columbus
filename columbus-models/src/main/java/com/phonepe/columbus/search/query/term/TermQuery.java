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
package com.phonepe.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents containing an exact term without analysis. Useful for exact matches on keywords, IDs, or tags. Prefer MATCH_QUERY for fields that require text analysis.")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TermQuery.TYPE)
public class TermQuery extends FieldQuery {

    public static final String TYPE = "TERM";

    @JsonPropertyDescription("The exact term value to match")
    private Object value;

    @JsonPropertyDescription("Whether to perform case-insensitive matching")
    private Boolean caseInsensitive;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
