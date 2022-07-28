package com.phonepe.platform.columbus.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.platform.columbus.core.constants.OperationConstants;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.operations.CreateIndexOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitiesSearchOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityDeleteOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityFieldsUpdateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitySaveOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityUpdateOperation;
import com.phonepe.platform.columbus.core.models.operations.MultiGetOperation;
import com.phonepe.platform.columbus.core.utils.CategoryTransformUtils;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.category.EntityStatus;
import com.phonepe.platform.columbus.models.core.category.Platform;
import com.phonepe.platform.columbus.models.core.category.StoredCategory;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.SneakyThrows;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryEntityCommandsTest extends BaseTest {

    @SneakyThrows
    @BeforeAll
    static void setupIndex() {
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
    void testCategoryEntitySave() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var StoredCategory = getStoredCategory();
        var documentId = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .value(StoredCategory)
                .build());

        var response = columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId);

        assertTrue(response.isPresent());

        var categoryEntity = response.get();
        categoryEntity.setUpdatedAt(0);

        var expectedCategoryEntity = CategoryTransformUtils.toModel(StoredCategory);
        expectedCategoryEntity.setUpdatedAt(0);

        assertEquals(expectedCategoryEntity, categoryEntity);
    }


    @Test
    void testCategoryEntityDelete() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var StoredCategory = getStoredCategory();
        var documentId = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .value(StoredCategory)
                .build());

        assertTrue(columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId)
            .isPresent());

        columbusCoreFeature.getCategoryEntityCommands()
            .entityDelete(EntityDeleteOperation.builder()
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .indexName(indexName)
                .build());

        assertFalse(columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId)
            .isPresent());
    }

    @Test
    void testJelasticSearch() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var StoredCategory = getStoredCategory();
        var documentId = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .value(StoredCategory)
                .build());

        assertTrue(columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId)
            .isPresent());

        var response = columbusCoreFeature.getCategoryEntityCommands()
            .jElasticSearch(getCategorySearchOperation(indexName));

        assertEquals(1, response.size());
        assertEquals(StoredCategory, response.get(0));

        columbusCoreFeature.getCategoryEntityCommands()
            .entityDelete(EntityDeleteOperation.builder()
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .indexName(indexName)
                .build());
    }


    @Test
    void testElasticSearch() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var StoredCategory = getStoredCategory();
        var documentId = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .value(StoredCategory)
                .build());

        assertTrue(columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId)
            .isPresent());

        var response = columbusCoreFeature.getCategoryEntityCommands()
            .elasticSearch(getCategorySearchOperation(indexName));

        assertEquals(1, response.size());

        assertEquals(StoredCategory, response.get(0));

        columbusCoreFeature.getCategoryEntityCommands()
            .entityDelete(EntityDeleteOperation.builder()
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .indexName(indexName)
                .build());
    }

    @Test
    void testMultiGetByIds() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var documentId1 = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId1)
                .value(getStoredCategory())
                .build());

        var documentId2 = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId2)
                .value(getStoredCategory())
                .build());

        var documentId3 = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId3)
                .value(getStoredCategory())
                .build());

        var response = columbusCoreFeature.getCategoryEntityCommands()
            .multiGetByIds(MultiGetOperation.<StoredCategory>builder()
                .documentIds(List.of(documentId1, documentId3))
                .entityType(EntityType.CATEGORY)
                .indexName(indexName)
                .entityClassType(StoredCategory.class)
                .build());

        assertEquals(2, response.size());
    }

    @Test
    void testEntityFieldUpdate() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var StoredCategory = getStoredCategory();
        var documentId = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .value(StoredCategory)
                .build());

        var response = columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId);

        assertTrue(response.isPresent());
        assertEquals("entityDescription", response.get().getDescription());

        Map<String, Object> updatedFieldMap = new HashMap<>();
        updatedFieldMap.put("description", "updated_description");

        columbusCoreFeature.getCategoryEntityCommands()
            .entityFieldsUpdate(EntityFieldsUpdateOperation.builder()
                .retryCount(OperationConstants.UPDATE_OPERATION_RETRY_COUNT)
                .documentId(documentId)
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .entityFieldValueData(updatedFieldMap)
                .build());

        var updatedResponse = columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId);

        assertTrue(updatedResponse.isPresent());
        assertEquals("updated_description", updatedResponse.get().getDescription());
    }

    @Test
    void testUpdateEntityOperation() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var StoredCategory = getStoredCategory();
        var documentId = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .value(StoredCategory)
                .build());

        var response = columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId);

        assertTrue(response.isPresent());
        assertEquals(StoredCategory.getDocumentId(), response.get().getDocumentId());

        var newStoredCategory = getStoredCategory();
        assertNotEquals(StoredCategory.getDocumentId(), newStoredCategory.getDocumentId());

        columbusCoreFeature.getCategoryEntityCommands()
            .entityUpdate(EntityUpdateOperation.<StoredCategory>builder()
                .documentId(documentId)
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .value(newStoredCategory)
                .build());

        var updatedResponse = columbusCoreFeature.getCategoryEntityCommands()
            .getById(TestTenant.TEST_TENANT, documentId);

        assertTrue(updatedResponse.isPresent());
        assertEquals(newStoredCategory.getDocumentId(), updatedResponse.get().getDocumentId());
    }

    @Test
    void testGetColumbusTree() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name());

        var StoredCategory = getStoredCategory();
        var documentId = UUID.randomUUID().toString();

        columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(documentId)
                .value(StoredCategory)
                .build());

        var tree = columbusCoreFeature.getCategoryEntityCommands()
            .getTree(TestTenant.TEST_TENANT, List.of(Platform.values()));

        var categoryEntity = tree.getNode(StoredCategory.getDocumentId());
        categoryEntity.setUpdatedAt(0);

        var expectedCategoryEntity = CategoryTransformUtils.toModel(StoredCategory);
        expectedCategoryEntity.setUpdatedAt(0);

        assertEquals(expectedCategoryEntity, categoryEntity);
    }

    @SneakyThrows
    @AfterAll
    static void cleanup() {
        columbusCoreFeature.getElasticClient().getClient().indices()
            .delete(new DeleteIndexRequest(IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name())),
                RequestOptions.DEFAULT);
    }

    private EntitiesSearchOperation<StoredCategory> getCategorySearchOperation(String indexName) {
        return EntitiesSearchOperation.<StoredCategory>builder()
            .entityType(EntityType.CATEGORY)
            .indexName(indexName)
            .valueClassType(StoredCategory.class)
            .filter(AndFilter.builder()
                .filter(GreaterThanFilter.<Number>builder()
                    .field("updatedAt")
                    .value(Value.exact(0))
                    .build())
                .filter(InFilter.<String>builder()
                    .field("status")
                    .values(Value.exact(
                        Arrays.stream(EntityStatus.values())
                            .map(Enum::name)
                            .toList()))
                    .build())
                .build())
            .sorter(FieldSorter.builder()
                .priority(1)
                .field("updatedAt")
                .order(SortOrder.ASC)
                .build())
            .pageNumber(0)
            .pageSize(1000)
            .build();
    }

    private StoredCategory getStoredCategory() {
        return StoredCategory.builder()
            .documentId(UUID.randomUUID().toString())
            .name("entityName")
            .displayName("entityDisplayName")
            .description("entityDescription")
            .score(0)
            .status(EntityStatus.CREATED)
            .outgoingCategories(List.of())
            .updatedAt(System.currentTimeMillis())
            .createdAt(System.currentTimeMillis())
            .supportedPlatforms(List.of(Platform.values()))
            .createdBy("test")
            .root(true)
            .build();
    }
}
