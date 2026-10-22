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
package com.phonepe.columbus.storage.models.result;

import com.phonepe.columbus.search.aggregation.Aggregation;
import com.phonepe.columbus.search.misc.result.Result;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * The result of {@link com.phonepe.columbus.storage.commands.search.SearchCommand SearchCommand}.
 *
 * @param <T> the type of the entities.
 */
@Data
@SuperBuilder(toBuilder = true)
public class SearchResult<T> {

    private final Result<T> result;

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Aggregation> aggregations;
}
