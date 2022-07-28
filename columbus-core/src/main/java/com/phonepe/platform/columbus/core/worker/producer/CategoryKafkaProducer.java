package com.phonepe.platform.columbus.core.worker.producer;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.models.core.request.category.CategoryServiceRequest;
import com.phonepe.platform.kafka.configs.KafkaConfig;
import com.phonepe.platform.kafka.configs.TopicConfig;
import com.phonepe.platform.kafka.core.serde.StringSerDe;
import com.phonepe.platform.serde.JacksonJsonSerDe;

public class CategoryKafkaProducer extends ColumbusKafkaProducer<String, CategoryServiceRequest> {
    public CategoryKafkaProducer(
        TopicConfig topicConfig,
        KafkaConfig kafkaConfig,
        ObjectMapper mapper,
        MetricRegistry metricRegistry
    ) {
        super(
            topicConfig,
            kafkaConfig,
            new StringSerDe(),
            new JacksonJsonSerDe<>(CategoryServiceRequest.class, mapper),
            metricRegistry
        );
    }
}
