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
package com.phonepe.columbus.elastic9.dao.node;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * Representation of {@link com.phonepe.columbus.dao.node.NodeId NodeId} for ElasticSearch.
 */
@Data
@SuperBuilder
public class ElasticNode {

    private final String shard;

    @Singular(value = "routing", ignoreNullCollections = true)
    private final Set<@NonNull String> routing;

    private final String preference;
}
