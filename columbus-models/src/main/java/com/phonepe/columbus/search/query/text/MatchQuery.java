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
package com.phonepe.columbus.search.query.text;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.text.Analysis;
import com.phonepe.columbus.search.misc.text.Fuzziness;
import com.phonepe.columbus.search.misc.text.Terms;
import com.phonepe.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Full-text query that analyzes the text and finds documents matching the analyzed terms")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchQuery.TYPE)
public class MatchQuery extends FieldQuery {

    public static final String TYPE = "MATCH";

    @JsonPropertyDescription("The text value to search for")
    private Object value;

    @JsonPropertyDescription("Text analysis settings including analyzer and operator")
    private Analysis analysis;

    @JsonPropertyDescription("Term matching settings like minimum should match")
    private Terms terms;

    @JsonPropertyDescription("Fuzzy matching configuration for handling typos")
    private Fuzziness fuzziness;

    @JsonPropertyDescription("Whether to ignore errors during query execution")
    private Boolean ignoreErrors;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
