package com.phonepe.platform.columbus.core.worker.consumer;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.phonepe.platform.columbus.core.models.worker.ColumbusWorkerConfig;
import com.phonepe.platform.columbus.core.models.worker.kafka.KafkaWorkerConfig;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.request.category.CategoryServiceRequest;
import com.phonepe.platform.kafka.configs.ConsumerConfig;
import com.phonepe.platform.kafka.configs.KafkaConfig;
import io.dropwizard.lifecycle.Managed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
@Singleton
public class ColumbusCategoryConsumerManager implements Managed {
    private final ColumbusWorkerConfig config;
    private final List<ColumbusCategoryConsumerManager.ColumbusCategoryConsumerWithExecutor> consumerWithExecutors;
    private final List<ColumbusConsumer<CategoryServiceRequest, Void>> columbusConsumers;

    @SuppressWarnings("java:S3958") // SonarQube fix
    public ColumbusCategoryConsumerManager(
        final ColumbusWorkerConfig columbusWorkerConfig,
        final List<ColumbusTenantType> tenantNames,
        final MetricRegistry metricRegistry,
        final ObjectMapper objectMapper,
        final CategoryService categoryService
    ) {
        this.consumerWithExecutors = new ArrayList<>();
        this.config = columbusWorkerConfig;

        this.columbusConsumers = tenantNames.stream()
            .map(ColumbusTenantType::name)
            .map(tenantName -> createConsumer(tenantName, metricRegistry, objectMapper, categoryService))
            .toList();
    }

    private ColumbusConsumer<CategoryServiceRequest, Void> createConsumer(
        final String configName,
        final MetricRegistry metricRegistry,
        final ObjectMapper objectMapper,
        final CategoryService categoryService
    ) {
        return this.config.accept(kafkaWorkerConfig -> {
            KafkaConfig kafkaConfig = kafkaWorkerConfig.getKafkaConfig();
            Map<String, ConsumerConfig> entityConfigMap = kafkaConfig.getAllConsumerConfig().getEntityConfigMap();

            // warn message if config for tenant not present then use default
            ConsumerConfig consumerConfig = entityConfigMap.getOrDefault(configName,
                entityConfigMap.get(KafkaWorkerConfig.DEFAULT_TOPIC_NAME_TEXT));

            return new CategoryKafkaConsumer(consumerConfig, kafkaConfig, objectMapper, metricRegistry, categoryService);
        });
    }

    @Override
    public void start() {
        columbusConsumers.forEach(consumer -> {
            ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(consumer.name() + "-%d")
                .build();

            ExecutorService executorService = Executors.newSingleThreadExecutor(threadFactory);
            ColumbusCategoryConsumerManager.ColumbusCategoryConsumerWithExecutor consumerWithExecutor =
                new ColumbusCategoryConsumerManager.ColumbusCategoryConsumerWithExecutor(executorService, consumer);

            consumerWithExecutors.add(consumerWithExecutor);
            log.info("Starting consumer instances for {}", consumer.name());
            executorService.submit(consumer);
        });
    }

    @Override
    public void stop() {
        this.consumerWithExecutors.forEach(consumerWithExecutor -> {
            String consumerName = consumerWithExecutor.consumer.name();
            log.info("Shutting down {} executor", consumerName);

            try {
                consumerWithExecutor.consumer.close();
            } catch (IOException var5) {
                log.error("Exception in closing consumer: {}", consumerName, var5);
            }

            try {
                @SuppressWarnings("UnstableApiUsage")
                boolean safeShutdown = MoreExecutors.shutdownAndAwaitTermination(
                    consumerWithExecutor.executorService,
                    this.config.getShutdownWaitTimeMs(),
                    TimeUnit.MILLISECONDS
                );
                log.info("Safe Shutdown of consumer: {}, safely:{}", consumerName, safeShutdown);
            } catch (Exception var4) {
                log.info("Consumer: {} did not shutdown", consumerName);
            }
        });
    }

    @Data
    @AllArgsConstructor
    private static class ColumbusCategoryConsumerWithExecutor {
        private final ExecutorService executorService;
        private final ColumbusConsumer<CategoryServiceRequest, Void> consumer;
    }
}
