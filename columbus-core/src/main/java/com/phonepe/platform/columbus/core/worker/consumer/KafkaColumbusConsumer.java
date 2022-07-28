package com.phonepe.platform.columbus.core.worker.consumer;

import com.codahale.metrics.MetricRegistry;
import com.github.rholder.retry.Retryer;
import com.phonepe.platform.kafka.configs.ConsumerConfig;
import com.phonepe.platform.kafka.configs.KafkaConfig;
import com.phonepe.platform.kafka.consumer.SKafkaConsumer;
import com.phonepe.platform.serde.SerDe;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public abstract class KafkaColumbusConsumer<K, M> extends SKafkaConsumer<K, M> implements ColumbusConsumer<M, Void> {
    protected KafkaColumbusConsumer(
        ConsumerConfig consumerConfig,
        KafkaConfig asyncConfig,
        SerDe<K> keySerDe,
        SerDe<M> messageSerDe,
        MetricRegistry metricRegistry
    ) {
        super(consumerConfig, asyncConfig, keySerDe, messageSerDe, metricRegistry);
    }

    @Override
    protected abstract boolean sideline(ConsumerRecord<K, M> consumerRecord);

    @Override
    protected abstract Retryer<Boolean> provideRetryer();

    @Override
    public void consume(M message) {
        run(message);
    }

    @Override
    public String name() {
        return getClass().getName();
    }
}
