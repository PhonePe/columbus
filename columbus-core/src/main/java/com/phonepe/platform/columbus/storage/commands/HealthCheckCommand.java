package com.phonepe.platform.columbus.storage.commands;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import lombok.Builder;
import lombok.Data;

/**
 * A command to check the health of the storage.
 */
@Data
@Builder
public class HealthCheckCommand implements Command<HealthStatus> {

    public static final String COMMAND_NAME = "HEALTH";

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    public enum HealthStatus {
        UNHEALTHY,
        DEGRADED,
        HEALTHY,
    }
}
