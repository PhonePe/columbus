package com.phonepe.platform.columbus.core.models.worker.kafka;

import com.phonepe.platform.columbus.core.models.worker.ColumbusWorkerConfig;
import com.phonepe.platform.columbus.core.models.worker.WorkerConfigType;
import com.phonepe.platform.columbus.core.models.worker.WorkerConfigVisitor;
import com.phonepe.platform.kafka.configs.KafkaConfig;
import io.dropwizard.validation.ValidationMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaWorkerConfig implements ColumbusWorkerConfig {
    public static final String DEFAULT_TOPIC_NAME_TEXT = "default_topic";

    @Valid
    @NotNull
    private KafkaConfig kafkaConfig;

    @Override
    public @NonNull WorkerConfigType getConfigType() {
        return WorkerConfigType.KAFKA_WORKER_CONFIG;
    }

    @Override
    public int getShutdownWaitTimeMs() {
        return kafkaConfig.getShutdownWaitTimeMs();
    }

    @Override
    public <T> T accept(WorkerConfigVisitor<T> visitor) {
        return visitor.visit(this);
    }

    private boolean isValidTopicConfig() {
        return null != kafkaConfig.getAllTopicConfig()
            && null != kafkaConfig.getAllTopicConfig().getEntityConfigMap()
            && !kafkaConfig.getAllTopicConfig().getEntityConfigMap().isEmpty()
            && null != kafkaConfig.getAllTopicConfig().getEntityConfigMap().get(DEFAULT_TOPIC_NAME_TEXT);
    }

    @ValidationMethod(message = "kafka worker config is not valid")
    public boolean isValidKafkaConfig() {
        return null != kafkaConfig.getConsumerConfig()
            && null != kafkaConfig.getProducerConfig()
            && isValidTopicConfig();
    }
}
