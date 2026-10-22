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
package com.phonepe.columbus.storage.commands.entity.bulk;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.misc.OperationFailure;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.columbus.storage.models.entity.EntityId;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A command to delete a batch of entities from an index.
 */
@Data
@Builder
public class BulkDeleteCommand implements Command<BulkDeleteCommand.Result> {

    public static final String COMMAND_NAME = "BULK_DELETE";

    @NonNull
    private final Index index;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull EntityId> ids;

    private final RequestExecutionPolicy executionPolicy;

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Data
    @Builder
    public static class Result {

        @Singular(value = "notFound", ignoreNullCollections = true)
        private final Set<Id> notFound;

        @Singular(value = "failure", ignoreNullCollections = true)
        private final Map<Id, OperationFailure> failure;

        public boolean hasFailures() {
            return !failure.isEmpty() || !notFound.isEmpty();
        }
    }
}
