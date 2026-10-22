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
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that wraps another query and assigns a constant score to all matching documents, ignoring relevance")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ConstantScoreQuery.TYPE)
public class ConstantScoreQuery extends AbstractQuery {

    public static final String TYPE = "CONSTANT_SCORE";

    @JsonPropertyDescription("The query to execute. All matching documents will receive the same score")
    private Query query;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
