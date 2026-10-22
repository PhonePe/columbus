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
import com.phonepe.columbus.search.query.AbstractQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Collection;
import java.util.Map;

@JsonClassDescription("Query that searches for the same value across multiple fields with optional field-specific boosting")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MultiMatchQuery.TYPE)
public class MultiMatchQuery extends AbstractQuery {

    public static final String TYPE = "MULTI_MATCH";

    @JsonPropertyDescription("Type of multi-match query (best_fields, most_fields, cross_fields, phrase, phrase_prefix, bool_prefix)")
    private String matchType;

    @JsonPropertyDescription("The text value to search for across multiple fields")
    private Object value;

    @JsonPropertyDescription("Map of field names to their boost values")
    @Singular(ignoreNullCollections = true)
    private Map<@NonNull String, @NonNull Float> fieldBoosts;

    @JsonPropertyDescription("Tie breaker for combining scores from multiple fields")
    private Float tieBreaker;

    @JsonPropertyDescription("Text analysis settings including analyzer and operator")
    private Analysis analysis;

    @JsonPropertyDescription("Term matching settings like minimum should match")
    private Terms terms;

    @JsonPropertyDescription("Fuzzy matching configuration for handling typos")
    private Fuzziness fuzziness;

    @JsonPropertyDescription("Maximum positions between terms for phrase queries")
    private Integer slop;

    @JsonPropertyDescription("Whether to ignore errors during query execution")
    private Boolean ignoreErrors;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }

    @SuppressWarnings("unused")
    public abstract static class MultiMatchQueryBuilder<C extends MultiMatchQuery,
            B extends MultiMatchQueryBuilder<C, B>> extends AbstractQueryBuilder<C, B> {

        public B fieldName(final String fieldName) {
            return fieldName == null ? self() : fieldBoost(fieldName, 1.0f);
        }

        public B fieldNames(final Collection<String> fieldNames) {
            if (fieldNames != null) {
                fieldNames.forEach(this::fieldName);
            }
            return self();
        }
    }
}
