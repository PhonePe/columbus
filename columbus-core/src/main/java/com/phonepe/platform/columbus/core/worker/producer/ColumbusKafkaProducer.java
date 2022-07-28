package com.phonepe.platform.columbus.core.worker.producer;

import com.codahale.metrics.MetricRegistry;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusIngestionErrorCode;
import com.phonepe.platform.kafka.configs.KafkaConfig;
import com.phonepe.platform.kafka.configs.TopicConfig;
import com.phonepe.platform.kafka.core.AsyncMeta;
import com.phonepe.platform.kafka.producer.SKafkaProducer;
import com.phonepe.platform.serde.SerDe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class ColumbusKafkaProducer<K, M> extends SKafkaProducer<K, M>
    implements ColumbusProducer<K, M, CompletableFuture<AsyncMeta>> {

    protected ColumbusKafkaProducer(
        TopicConfig topicConfig,
        KafkaConfig kafkaConfig,
        SerDe<K> keySerDe,
        SerDe<M> messageSerDe,
        MetricRegistry metricRegistry
    ) {
        super(topicConfig.getTopicName(), kafkaConfig, keySerDe, messageSerDe, metricRegistry);
    }

    @Override
    public CompletableFuture<AsyncMeta> publish(K key, M message) {
        log.info("Async Ingestion key: {}", MapperUtils.writeValueAsString(key));
        log.info("Async Ingestion message: {}", MapperUtils.writeValueAsString(message));
        try {
            CompletableFuture<AsyncMeta> completableFuture = this.produceAsync(key, message);
            completableFuture.whenComplete((asyncMeta, throwable) -> log.info("Published {}", asyncMeta));
            return completableFuture;
        } catch (Exception e) {
            log.error("Exception occurred in kafka ingestion", e);
            throw ColumbusException.propagate(ColumbusIngestionErrorCode.KAFKA_EXCEPTION,
                "Kafka Ingestion Exception", e);
        }
    }
}