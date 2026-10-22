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
package com.phonepe.columbus.elastic7.internal.storage.utils;

import com.phonepe.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.columbus.storage.models.result.UpsertResult;
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
            case NOOP -> UpsertResult.UNALTERED;
            case DELETED, NOT_FOUND ->
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
