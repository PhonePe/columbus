package com.phonepe.platform.columbus.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.operations.CreateIndexOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import lombok.SneakyThrows;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryIndexManageCommandsTest extends BaseTest {

    @Test
    void testCreateIndex() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());
        assertFalse(columbusCoreFeature.getCategoryIndexManageCommands().existsIndex(indexName));

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
                .entityType(EntityType.CATEGORY)
                .indexName(indexName).build());

        assertTrue(columbusCoreFeature.getCategoryIndexManageCommands().existsIndex(indexName));
    }

    @SneakyThrows
    @AfterAll
    static void cleanup() {
        columbusCoreFeature.getElasticClient().getClient().indices()
            .delete(new DeleteIndexRequest(IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name())),
                RequestOptions.DEFAULT);
    }


}
