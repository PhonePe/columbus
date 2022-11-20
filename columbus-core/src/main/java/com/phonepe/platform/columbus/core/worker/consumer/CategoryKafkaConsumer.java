package com.phonepe.platform.columbus.core.worker.consumer;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusErrorCode;
import com.phonepe.platform.columbus.models.core.request.category.*;
import com.phonepe.platform.kafka.configs.ConsumerConfig;
import com.phonepe.platform.kafka.configs.KafkaConfig;
import com.phonepe.platform.kafka.core.serde.StringSerDe;
import com.phonepe.platform.serde.JacksonJsonSerDe;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CategoryKafkaConsumer extends KafkaColumbusConsumer<String, CategoryServiceRequest> {
    private final CategoryService categoryService;

    public CategoryKafkaConsumer(
        final ConsumerConfig consumerConfig,
        final KafkaConfig asyncConfig,
        final ObjectMapper mapper,
        final MetricRegistry metricRegistry,
        final CategoryService categoryService
    ) {
        super(
            consumerConfig,
            asyncConfig,
            new StringSerDe(),
            new JacksonJsonSerDe<>(CategoryServiceRequest.class, mapper),
            metricRegistry
        );
        this.categoryService = categoryService;
    }

    @Override
    protected boolean sideline(ConsumerRecord<String, CategoryServiceRequest> consumerRecord) {
        //TODO add sideline publisher
        return true;
    }

    @Override
    protected Retryer<Boolean> provideRetryer() {
        return RetryerBuilder.<Boolean>newBuilder()
            .retryIfExceptionOfType(ColumbusException.class)
            .retryIfRuntimeException()
            .retryIfException()
            .withWaitStrategy(WaitStrategies.exponentialWait(5, 10, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(3))
            .build();
    }

    @Override
    public Void run(CategoryServiceRequest serviceRequest) {
        try {
            serviceRequest.accept(new CategoryServiceRequestVisitor<Void>() {
                @Override
                public Void visit(InitialiseServiceRequest initialiseServiceRequest) {
                    categoryService.initialise(initialiseServiceRequest);
                    return null;
                }

                @Override
                public Void visit(UpdateMappingServiceRequest updateMappingServiceRequest) {
                    categoryService.updateMapping(updateMappingServiceRequest);
                    return null;
                }

                @Override
                public Void visit(UpdateCategoryLinkRequest updateCategoryLinkRequest) {
                    return null;
                }

                @Override
                public Void visit(UpdateCategoryFieldsRequest updateCategoryFieldsRequest) {
                    categoryService.update(updateCategoryFieldsRequest);
                    return null;
                }

                @Override
                public Void visit(SearchCategoryRequest searchCategoryRequest) {
                    return null;
                }

                @Override
                public Void visit(CreateCategoryRequest createCategoryRequest) {
                    categoryService.create(createCategoryRequest);
                    return null;
                }

                @Override
                public Void visit(DeleteCategoryRequest deleteCategoryRequest) {
                    categoryService.delete(deleteCategoryRequest);
                    return null;
                }
            });
            return null;
        } catch (Exception ex) {
            log.error("Error while processing service request of type {} and tenant {}",
                serviceRequest.getRequestType(), serviceRequest.getTenantType(), ex);
            throw ColumbusException.propagate(ColumbusErrorCode.INTERNAL_ERROR, ex);
        }
    }
}
