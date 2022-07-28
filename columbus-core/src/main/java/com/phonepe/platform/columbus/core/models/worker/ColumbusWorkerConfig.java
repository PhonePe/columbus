package com.phonepe.platform.columbus.core.models.worker;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.platform.columbus.core.models.worker.kafka.KafkaWorkerConfig;
import lombok.NonNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "configType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = KafkaWorkerConfig.class, name = "KAFKA_WORKER_CONFIG")
})
public interface ColumbusWorkerConfig {
    @NonNull WorkerConfigType getConfigType();

    int getShutdownWaitTimeMs();

    <T> T accept(WorkerConfigVisitor<T> visitor);
}
