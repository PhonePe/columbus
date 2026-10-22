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
package com.phonepe.columbus.dao.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * An id with multiple keys to decide the shard to choose.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(RoutingKey.TYPE)
public class RoutingKey implements NodeId {

    public static final String TYPE = "ROUTING";

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull String> keys;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
