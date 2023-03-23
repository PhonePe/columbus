package com.phonepe.platform.columbus.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.operations.CreateIndexOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitySaveOperation;
import com.phonepe.platform.columbus.core.models.operations.UnitSearchOperation;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.SneakyThrows;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

class UnitEntityCommandsTest extends BaseTest {

    @SneakyThrows
    @BeforeAll
    static void setupIndex() {
        var unitName = TestUnit.class.getSimpleName();
        String indexName = IndexUtils.getIndexName(EntityType.UNIT, unitName);

        HashMap<String, Object> mappings = IndexUtils.getSource(
            "test_unit_mapping.json",
            new TypeReference<>() {
            });

        HashMap<String, Object> analysis = IndexUtils.getSource(
            "analysers/category_index_analysis.json",
            new TypeReference<>() {
            });

        columbusCoreFeature.getUnitIndexManageCommands()
            .createIndexTemplate(CreateTemplateOperation.builder()
                .indexName(indexName)
                .entityType(EntityType.UNIT)
                .indexPattern(IndexUtils.getIndexPattern(indexName))
                .templateName(IndexUtils.getTemplateName(indexName))
                .mappings(mappings)
                .analysis(analysis)
                .noOfReplicas(1)
                .noOfRoutingShards(1)
                .noOfShards(1)
                .build());

        columbusCoreFeature.getUnitIndexManageCommands()
            .createIndex(CreateIndexOperation.builder()
                .indexName(indexName)
                .entityType(EntityType.UNIT)
                .build());

        addTestData();
    }

    static void addTestData() {
        final var inputStream = UnitEntityCommandsTest.class.getClassLoader()
            .getResourceAsStream("testUnitData.json");
        var list = MapperUtils.readValue(inputStream,
            new TypeReference<List<TestUnit>>() {
            });

        String indexName = IndexUtils.getIndexName(EntityType.UNIT,
            TestUnit.class.getSimpleName());

        list.forEach(testUnit -> columbusCoreFeature.getUnitEntityCommands()
            .entitySave(EntitySaveOperation.<TestUnit>builder()
                .indexName(indexName)
                .entityType(EntityType.UNIT)
                .documentId(testUnit.getCarId())
                .value(testUnit)
                .build()));
    }

    @Test
    void testSearch() {
        var indexName = IndexUtils.getIndexName(EntityType.UNIT, TestUnit.class.getSimpleName());

        var response = columbusCoreFeature.getUnitEntityCommands()
            .search(getUnitSearchOperation(indexName, "car1"));

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("id101", response.get(0).getCarId());

        var response2 = columbusCoreFeature.getUnitEntityCommands()
            .search(getUnitSearchOperation(indexName, "car2"));

        Assertions.assertEquals(1, response2.size());
        Assertions.assertEquals("id102", response2.get(0).getCarId());
    }

    @Test
    void testJelasticSearch() {
        var indexName = IndexUtils.getIndexName(EntityType.UNIT, TestUnit.class.getSimpleName());

        var response = columbusCoreFeature.getUnitEntityCommands()
            .jElasticSearch(getUnitSearchOperation(indexName, "car1"));

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("id101", response.get(0).getCarId());

        var response2 = columbusCoreFeature.getUnitEntityCommands()
            .jElasticSearch(getUnitSearchOperation(indexName, "car3"));

        Assertions.assertEquals(1, response2.size());
        Assertions.assertEquals("id103", response2.get(0).getCarId());
    }

    @Test
    void testElasticSearch() {
        var indexName = IndexUtils.getIndexName(EntityType.UNIT, TestUnit.class.getSimpleName());

        var response = columbusCoreFeature.getUnitEntityCommands()
            .elasticSearch(getUnitSearchOperation(indexName, "car2"));

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("id102", response.get(0).getCarId());

        var response2 = columbusCoreFeature.getUnitEntityCommands()
            .elasticSearch(getUnitSearchOperation(indexName, "car3"));

        Assertions.assertEquals(1, response2.size());
        Assertions.assertEquals("id103", response2.get(0).getCarId());
    }

    @Test
    void testPaginatedElasticSearch() {
        var indexName = IndexUtils.getIndexName(EntityType.UNIT, TestUnit.class.getSimpleName());

        var response = columbusCoreFeature.getUnitEntityCommands()
            .paginatedElasticSearch(getUnitSearchOperation(indexName, "car2"));

        Assertions.assertEquals(1, response.getCount());
        Assertions.assertEquals(1, response.getEntities().size());
        Assertions.assertEquals("id102", response.getEntities().get(0).getCarId());
    }

    private UnitSearchOperation<TestUnit> getUnitSearchOperation(String indexName,
                                                                 String filterValue) {
        return UnitSearchOperation.<TestUnit>builder()
            .entityType(EntityType.UNIT)
            .indexName(indexName)
            .valueClassType(TestUnit.class)
            .queryFilter(EqualsFilter.builder()
                .field("carName")
                .value(Value.exact(filterValue))
                .build())
            .pageNumber(0)
            .pageSize(1000)
            .build();
    }

    @SneakyThrows
    @AfterAll
    static void cleanup() {
        columbusCoreFeature.getElasticClient().getClient().indices()
            .delete(new DeleteIndexRequest(
                    IndexUtils.getIndexName(EntityType.UNIT, TestUnit.class.getSimpleName())),
                RequestOptions.DEFAULT);
    }


}
