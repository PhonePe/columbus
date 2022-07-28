package com.phonepe.platform.columbus.core.models.worker;


import com.phonepe.platform.columbus.core.models.worker.kafka.KafkaWorkerConfig;

public interface WorkerConfigVisitor<T> {
    T visit(KafkaWorkerConfig kafkaWorkerConfig);
}
