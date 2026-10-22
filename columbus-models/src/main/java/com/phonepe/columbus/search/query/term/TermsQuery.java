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
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Query that matches documents where the field contains any of the specified terms")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TermsQuery.TYPE)
public class TermsQuery extends FieldQuery {

    public static final String TYPE = "TERMS";

    @JsonPropertyDescription("List of exact term values to match (documents matching any term will be returned)")
    @Singular(ignoreNullCollections = true)
    private List<Object> values;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
