package com.phonepe.commons.columbus.elastic9.internal.storage.utils;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.commons.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.commons.columbus.storage.models.result.UpsertResult;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to transform enums between Columbus and ElasticSearch.
 */
@UtilityClass
public class EnumTransforms {

    public HealthStatus toHealthStatus(final co.elastic.clients.elasticsearch._types.HealthStatus status) {
        return switch (status) {
            case Green -> HealthStatus.HEALTHY;
            case Yellow -> HealthStatus.DEGRADED;
            // Red, Unknown and Unavailable are considered unhealthy.
            default -> HealthStatus.UNHEALTHY;
        };
    }

    public UpsertResult toUpsertResult(final Result result) {
        return toUpsertResult(result.jsonValue());
    }

    public UpsertResult toUpsertResult(final String result) {
        return switch (result) {
            case "created" -> UpsertResult.CREATED;
            case "updated" -> UpsertResult.UPDATED;
            case "noop" -> UpsertResult.UNALTERED;
            default ->
                    throw new IllegalArgumentException("Invalid result for upsert: '%s'".formatted(result));
        };
    }

    public static Refresh toRefreshPolicy(final RequestExecutionPolicy executionPolicy) {
        return switch (executionPolicy) {
            case SUBMIT -> Refresh.False;
            case WAIT_FOR_COMPLETION -> Refresh.WaitFor;
            case FORCE_COMPLETION -> Refresh.True;
        };
    }
}
