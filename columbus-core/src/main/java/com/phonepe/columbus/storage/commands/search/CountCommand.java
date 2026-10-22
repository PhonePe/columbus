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
package com.phonepe.columbus.storage.commands.search;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.search.misc.result.TotalCount;
import com.phonepe.columbus.search.query.Query;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

/**
 * A command to count entities matching a query in an index.
 * <p>
 * When an approximate count is acceptable beyond a certain limit, {@link #threshold} can be specified.
 */
@Data
@Builder
public class CountCommand implements Command<TotalCount> {

    public static final String COMMAND_NAME = "COUNT";

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Index> indices;

    private final NodeId nodeId;

    private final Query query;

    private final Long threshold;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
