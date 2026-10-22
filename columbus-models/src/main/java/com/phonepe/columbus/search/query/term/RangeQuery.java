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
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents where the field value falls within a specified range")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RangeQuery.TYPE)
public class RangeQuery extends FieldQuery {

    public static final String TYPE = "RANGE";

    @JsonPropertyDescription("Lower bound of the range (inclusive or exclusive based on includeLower)")
    private Object from;

    @JsonPropertyDescription("Upper bound of the range (inclusive or exclusive based on includeUpper)")
    private Object to;

    @JsonPropertyDescription("Whether to include the lower bound value in the range")
    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeLower = true;

    @JsonPropertyDescription("Whether to include the upper bound value in the range")
    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeUpper = true;

    @JsonPropertyDescription("Timezone for date range queries (e.g., 'America/New_York', '+05:30')")
    private String timezone;

    @JsonPropertyDescription("Date format for parsing date strings")
    private String format;

    @JsonPropertyDescription("Relation between the range and the field values (intersects, contains, within)")
    private String relation;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
