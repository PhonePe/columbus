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
package com.phonepe.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@JsonClassDescription("Bucket aggregator that creates multiple named buckets, each defined by a filter query")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FiltersAggregator.TYPE)
public class FiltersAggregator extends BucketAggregator {

    public static final String TYPE = "FILTERS";

    @JsonPropertyDescription("Map of bucket names to their corresponding filter queries")
    @Singular(ignoreNullCollections = true)
    private Map<@NonNull String, @NonNull Query> filters;

    @JsonPropertyDescription("Name for an optional bucket that collects all documents not matching any filter")
    private String otherBucketKey;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
