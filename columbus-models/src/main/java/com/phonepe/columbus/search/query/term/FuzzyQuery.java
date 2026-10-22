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
import com.phonepe.columbus.search.misc.text.Fuzziness;
import com.phonepe.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents using fuzzy matching to find terms similar to the search value")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FuzzyQuery.TYPE)
public class FuzzyQuery extends FieldQuery {

    public static final String TYPE = "FUZZY";

    @JsonPropertyDescription("The term value to fuzzy match against")
    private Object value;

    @JsonPropertyDescription("Fuzziness configuration specifying the maximum edit distance (AUTO, 0, 1, 2)")
    private Fuzziness fuzziness;

    @JsonPropertyDescription("Maximum number of terms to which the query will expand")
    private Integer maximumExpansions;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
