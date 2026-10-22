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
package com.phonepe.columbus.storage.commands.entity.status;

import com.phonepe.columbus.command.Command;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Optional;

@Data
@Builder
public class GetCopyStatusCommand implements Command<Optional<GetCopyStatusCommand.Result>> {

    public static final String COMMAND_NAME = "COPY_STATUS";

    @NonNull
    private final String taskId;

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Data
    @Builder
    public static class Result {

        private final boolean completed;

        private final long startTime;

        private final long elapsedTime;

        private final long total;

        private final long created;

        private final long updated;
    }
}
