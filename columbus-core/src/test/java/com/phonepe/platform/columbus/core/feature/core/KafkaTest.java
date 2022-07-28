package com.phonepe.platform.columbus.core.feature.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.phonepe.platform.columbus.core.BaseTest;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.operations.CreateIndexOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import lombok.SneakyThrows;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

class KafkaTest extends BaseTest {
    @BeforeAll
    static void setupIndex() {
        columbusCoreFeature.getColumbusCategoryConsumerManager().start();

        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        HashMap<String, Object> mappings = IndexUtils.getSource(
            "mappings/category_storage_entity.json",
            new TypeReference<>() {
            });

        HashMap<String, Object> analysis = IndexUtils.getSource(
            "analysers/category_index_analysis.json",
            new TypeReference<>() {
            });

        columbusCoreFeature.getCategoryIndexManageCommands()
            .createIndexTemplate(CreateTemplateOperation.builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .indexPattern(IndexUtils.getIndexPattern(indexName))
                .templateName(IndexUtils.getTemplateName(indexName))
                .mappings(mappings)
                .analysis(analysis)
                .noOfReplicas(1)
                .noOfRoutingShards(1)
                .noOfShards(1)
                .build());

        columbusCoreFeature.getCategoryIndexManageCommands()
            .createIndex(CreateIndexOperation.builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .build());
    }

    @Test
    void test() {
        columbusCoreFeature.getColumbusCategoryProducerRegistry()
            .publish(CreateCategoryRequest.builder()
                .id("Abcd")
                .name("abcd")
                .displayName("ab")
                .tenantType(TestTenant.TEST_TENANT)
                .build());

        final Callable<Optional<Category>> callable = () -> columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, "Abcd");

        final var retryer = RetryerBuilder.<Optional<Category>>newBuilder()
            .retryIfException()
            .retryIfResult(Optional::isEmpty)
            .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
            .withStopStrategy(StopStrategies.stopAfterDelay(10, TimeUnit.SECONDS))
            .build();

        try {
            retryer.call(callable);
        } catch (final ExecutionException ex) {
            fail("Exception occurred while asserting");
        } catch (final RetryException ex) {
            fail("Assestion Failed");
        }
    }

    @SneakyThrows
    @AfterAll
    static void cleanup() {
        columbusCoreFeature.getElasticClient().getClient().indices()
            .delete(new DeleteIndexRequest(IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name())),
                RequestOptions.DEFAULT);
    }
}
