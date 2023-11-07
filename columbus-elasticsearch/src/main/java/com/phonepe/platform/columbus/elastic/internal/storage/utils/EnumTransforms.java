package com.phonepe.platform.columbus.elastic.internal.storage.utils;

import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.platform.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.platform.columbus.storage.models.result.UpsertResult;
import lombok.experimental.UtilityClass;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
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

    public UpsertResult toUpsertResult(final DocWriteResponse.Result result) {
        return switch (result) {
            case CREATED -> UpsertResult.CREATED;
            case UPDATED -> UpsertResult.UPDATED;
            case DELETED, NOT_FOUND, NOOP ->
                    throw new IllegalArgumentException("Invalid result for upsert: '%s'".formatted(result));
        };
    }

    public static RefreshPolicy toRefreshPolicy(final RequestExecutionPolicy executionPolicy) {
        return switch (executionPolicy) {
            case SUBMIT -> RefreshPolicy.NONE;
            case WAIT_FOR_COMPLETION -> RefreshPolicy.WAIT_UNTIL;
            case FORCE_COMPLETION -> RefreshPolicy.IMMEDIATE;
        };
    }
}
