package com.phonepe.platform.columbus.core.worker.producer;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.models.worker.ColumbusWorkerConfig;
import com.phonepe.platform.columbus.core.models.worker.kafka.KafkaWorkerConfig;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.request.category.CategoryServiceRequest;
import com.phonepe.platform.kafka.configs.KafkaConfig;
import com.phonepe.platform.kafka.configs.TopicConfig;
import com.phonepe.platform.kafka.core.AsyncMeta;
import io.dropwizard.lifecycle.Managed;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class ColumbusCategoryProducerRegistry implements Managed {
    @Getter
    private final Map<String, ColumbusProducer<String, CategoryServiceRequest, CompletableFuture<AsyncMeta>>> columbusProducers;

    public ColumbusCategoryProducerRegistry(
        final ColumbusWorkerConfig workerConfig,
        final List<ColumbusTenantType> tenantNames,
        final MetricRegistry metricRegistry,
        final ObjectMapper mapper
    ) {
        columbusProducers = tenantNames.stream()
            .map(ColumbusTenantType::name)
            .map(this::getProducerName)
            .collect(Collectors.toConcurrentMap(
                Function.identity(),
                name -> createProducer(workerConfig, name, metricRegistry, mapper)
            ));
    }

    private ColumbusProducer<String, CategoryServiceRequest, CompletableFuture<AsyncMeta>> createProducer(
        final ColumbusWorkerConfig workerConfig,
        final String topicName,
        final MetricRegistry metricRegistry,
        final ObjectMapper mapper
    ) {
        return workerConfig.accept(kafkaWorkerConfig -> {
            KafkaConfig kafkaConfig = kafkaWorkerConfig.getKafkaConfig();
            Map<String, TopicConfig> entityConfigMap = kafkaConfig.getAllTopicConfig().getEntityConfigMap();

            // warn message if config for tenant not present then use default
            TopicConfig topicConfig = entityConfigMap.getOrDefault(topicName,
                entityConfigMap.get(getProducerName(KafkaWorkerConfig.DEFAULT_TOPIC_NAME_TEXT)));

            return new CategoryKafkaProducer(topicConfig, kafkaConfig, mapper, metricRegistry);
        });
    }

    private String getProducerName(String producerName) {
        return producerName;
    }

    public void publish(final CategoryServiceRequest categoryServiceRequest) {
        publish(categoryServiceRequest.getTenantType().name(), categoryServiceRequest);
    }

    public void publish(final String key, final CategoryServiceRequest categoryServiceRequest) {
        this.columbusProducers.get(getProducerName(categoryServiceRequest.getTenantType().name()))
            .publish(key, categoryServiceRequest);
    }

    @Override
    public void start() {
        log.info("Started Columbus Category Producer registry");
    }

    @Override
    public void stop() {
        columbusProducers.forEach((name, columbusProducer) -> {
            try {
                columbusProducer.close();
            } catch (IOException e) {
                log.error("Error while closing columbus producer '%s'".formatted(name), e);
            }
        });
    }
}
