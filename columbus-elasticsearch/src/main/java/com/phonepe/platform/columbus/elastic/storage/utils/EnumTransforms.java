package com.phonepe.platform.columbus.elastic.storage.utils;

import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import lombok.experimental.UtilityClass;
import org.elasticsearch.cluster.health.ClusterHealthStatus;

/**
 * A collection of helper methods to transform enums between Columbus and ElasticSearch.
 */
@UtilityClass
public class EnumTransforms {

    public HealthStatus toHealthStatus(final ClusterHealthStatus status) {
        return switch (status) {
            case GREEN -> HealthStatus.HEALTHY;
            case YELLOW -> HealthStatus.DEGRADED;
            case RED -> HealthStatus.UNHEALTHY;
        };
    }
}
