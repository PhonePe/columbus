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
package com.phonepe.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.AbstractQuery;
import com.phonepe.columbus.search.query.Query;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that boosts documents matching the positive query while demoting those matching the negative query")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(BoostingQuery.TYPE)
public class BoostingQuery extends AbstractQuery {

    public static final String TYPE = "BOOSTING";

    @JsonPropertyDescription("Query for documents that should receive a positive boost")
    private Query positive;

    @JsonPropertyDescription("Query for documents that should have their scores reduced")
    private Query negative;

    @JsonPropertyDescription("Factor by which to multiply the score of documents matching the negative query (0.0 to 1.0)")
    @Builder.Default
    private float negativeBoost = 1f;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
