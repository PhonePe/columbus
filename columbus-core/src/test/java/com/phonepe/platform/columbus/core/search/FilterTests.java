package com.phonepe.platform.columbus.core.search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.platform.columbus.core.BaseTest;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.core.models.operations.CreateIndexOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitiesSearchOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitySaveOperation;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.category.StoredCategory;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.NotEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.WildCardFilter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.OrFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.BetweenFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessThanFilter;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.SneakyThrows;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterTests extends BaseTest {

    @SneakyThrows
    @BeforeAll
    static void setupIndex() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

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

        addTestData();
    }

    static void addTestData() {
        final var inputStream = FilterTests.class.getClassLoader()
            .getResourceAsStream("testData.json");
        var list = MapperUtils.readValue(inputStream,
            new TypeReference<List<StoredCategory>>() {
            });

        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        list.forEach(categoryStorageEntity -> columbusCoreFeature.getCategoryEntityCommands()
            .entitySave(EntitySaveOperation.<StoredCategory>builder()
                .indexName(indexName)
                .entityType(EntityType.CATEGORY)
                .documentId(categoryStorageEntity.getDocumentId())
                .value(categoryStorageEntity)
                .build()));
    }

    @Test
    void testGreaterThanFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            GreaterThanFilter.builder()
                .field("score")
                .value(Value.exact(0))
                .build(),
            Set.of("id2", "id3")
        );
    }


    @Test
    void testGreaterThanEqualFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            GreaterEqualsFilter.builder()
                .field("score")
                .value(Value.exact(0))
                .build(),
            Set.of("id1", "id2", "id3")
        );
    }

    @Test
    void testLessThanFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            LessThanFilter.builder()
                .value(Value.exact(10))
                .field("score")
                .build(),
            Set.of("id1")
        );
    }

    @Test
    void testLessThanEqualFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            LessEqualsFilter.builder()
                .value(Value.exact(
                    10)).field("score")
                .build(),
            Set.of("id1", "id2")
        );
    }

    @Test
    void testEqualFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            EqualsFilter.builder()
                .field("name")
                .value(Value.exact("name2"))
                .build(),
            Set.of("id2")
        );
    }

    @Test
    void testWildcardTesting() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        var filter = WildCardFilter.builder()
            .field("name")
            .value(Value.exact("name*"))
            .build();

        var elasticResponse = columbusCoreFeature.getCategoryEntityCommands()
            .elasticSearch(getCategorySearchOperation(indexName, filter));

        assertResponseIds(Set.of("id1", "id2", "id3"), elasticResponse);
    }

    @Test
    void testNotEqualFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            NotEqualsFilter.builder()
                .field("name")
                .value(Value.exact("name2"))
                .build(),
            Set.of("id1", "id3")
        );
    }

    @Test
    void testInFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            InFilter.builder()
                .field("name")
                .values(Value.exact(List.of("name2", "name3")))
                .build(),
            Set.of("id2", "id3")
        );

        assertFilterHelper(
            indexName,
            InFilter.builder()
                .field("name")
                .values(Value.exact(List.of("name1", "name3")))
                .build(),
            Set.of("id1", "id3")
        );
    }

    @Test
    void testAndFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            AndFilter.builder()
                .filter(GreaterThanFilter.builder()
                    .field("score")
                    .value(Value.exact(0))
                    .build())
                .filter(NotEqualsFilter.builder()
                    .field("name")
                    .value(Value.exact("name3"))
                    .build())
                .build(),
            Set.of("id2")
        );

        assertFilterHelper(
            indexName,
            AndFilter.builder()
                .filter(GreaterThanFilter.builder()
                    .field("score")
                    .value(Value.exact(0))
                    .build())
                .filter(EqualsFilter.builder()
                    .field("name")
                    .value(Value.exact("name1"))
                    .build())
                .build(),
            Set.of()
        );
    }


    @Test
    void testOrFilter() {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY,
            TestTenant.TEST_TENANT.name());

        assertFilterHelper(
            indexName,
            OrFilter.builder()
                .filter(GreaterThanFilter.builder()
                    .field("score")
                    .value(Value.exact(0))
                    .build())
                .filter(InFilter.builder()
                    .field("name")
                    .values(Value.exact(List.of("name2", "name3")))
                    .build())
                .build(),
            Set.of("id2", "id3")
        );
    }


    void assertFilterHelper(String indexName, Filter filter, Set<String> ids) {
        var jElasticResponse = columbusCoreFeature.getCategoryEntityCommands()
            .jElasticSearch(getCategorySearchOperation(indexName, filter));

        assertResponseIds(ids, jElasticResponse);

        var elasticResponse = columbusCoreFeature.getCategoryEntityCommands()
            .elasticSearch(getCategorySearchOperation(indexName, filter));

        assertResponseIds(ids, elasticResponse);

        assertEquals(jElasticResponse, elasticResponse);
    }

    private void assertResponseIds(Set<String> expectedIds, List<StoredCategory> searchResponse) {
        assertEquals(expectedIds.size(), searchResponse.size());

        var receivedIds = searchResponse.stream()
            .map(StoredCategory::getDocumentId)
            .collect(Collectors.toSet());

        expectedIds.forEach(id -> assertTrue(receivedIds.contains(id)));
    }


    private EntitiesSearchOperation<StoredCategory> getCategorySearchOperation(String indexName,
        Filter filter) {
        return EntitiesSearchOperation.<StoredCategory>builder()
            .entityType(EntityType.CATEGORY)
            .indexName(indexName)
            .valueClassType(StoredCategory.class)
            .filter(filter)
            .pageNumber(0)
            .pageSize(1000)
            .build();
    }

    @SneakyThrows
    @AfterAll
    static void cleanup() {
        columbusCoreFeature.getElasticClient().getClient().indices()
            .delete(new DeleteIndexRequest(
                    IndexUtils.getIndexName(EntityType.CATEGORY, TestTenant.TEST_TENANT.name())),
                RequestOptions.DEFAULT);
    }
}
