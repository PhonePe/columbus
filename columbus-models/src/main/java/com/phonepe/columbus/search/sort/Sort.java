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
package com.phonepe.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * A sort defines the order of the documents in the result.
 * <p>
 * The {@link #getOrder() order} can be changed to reverse the criteria.
 */
@JsonClassDescription("Interface for defining sort criteria to order documents in search results")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(FieldSort.class),
        @JsonSubTypes.Type(ScoreSort.class),
        @JsonSubTypes.Type(GeoDistanceSort.class),
        @JsonSubTypes.Type(ScriptSort.class)
})
public interface Sort {

    @JsonPropertyDescription("Sort order direction (ASC for ascending, DESC for descending)")
    @NonNull
    SortOrder getOrder();

    @JsonPropertyDescription("The type identifier for this sort")
    @NonNull
    String getType();
}
