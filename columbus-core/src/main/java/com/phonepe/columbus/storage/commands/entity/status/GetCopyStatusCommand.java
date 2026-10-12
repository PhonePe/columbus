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
