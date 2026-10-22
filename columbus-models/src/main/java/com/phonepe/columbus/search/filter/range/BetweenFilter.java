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
package com.phonepe.columbus.search.filter.range;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.filter.FieldFilter;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Filter that matches documents where the field value is between two specified bounds")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(BetweenFilter.TYPE)
public class BetweenFilter extends FieldFilter {

    public static final String TYPE = "BETWEEN";

    @JsonPropertyDescription("Lower bound of the range")
    private Object from;

    @JsonPropertyDescription("Upper bound of the range")
    private Object to;

    @JsonPropertyDescription("Whether to include the lower bound value in the range")
    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeLower = true;

    @JsonPropertyDescription("Whether to include the upper bound value in the range")
    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeUpper = true;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
