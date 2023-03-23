package com.phonepe.platform.columbus.core.commands.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.grookage.jelastic.core.exception.JelasticException;
import com.grookage.jelastic.core.repository.ElasticRepository;
import com.phonepe.platform.columbus.core.TestUnit;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.operations.EntityDeleteOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityFieldsUpdateOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityGetOperation;
import com.phonepe.platform.columbus.core.models.operations.EntitySaveOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityUpdateOperation;
import com.phonepe.platform.columbus.core.models.operations.MultiGetOperation;
import com.phonepe.platform.columbus.core.models.operations.UnitSearchOperation;
import com.phonepe.platform.columbus.core.search.resolver.SearchQueryBuilder;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedSorter;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.core.utils.ReflectionUtils;
import com.phonepe.platform.columbus.core.visitors.Visitor;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UnitEntityCommandsFailTest {
    @Mock
    ElasticRepository elasticRepository;
    ObjectMapper mapper;
    @Mock
    SearchQueryBuilder searchQueryBuilder;

    UnitEntityCommands unitEntityCommands;

    UnitSearchOperation<TestUnit> unitSearchOperation;

    @BeforeEach
    void setup() {
        mapper = Jackson.newObjectMapper(new YAMLFactory());
        mapper.registerSubtypes(List.copyOf(ReflectionUtils.getJsonSubtypes(Filter.class)));
        mapper.registerSubtypes(List.copyOf(ReflectionUtils.getJsonSubtypes(Visitor.class)));
        MapperUtils.initialise(mapper);
        unitEntityCommands = new UnitEntityCommands(elasticRepository, mapper, searchQueryBuilder);
        unitSearchOperation = getUnitSearchOperation();

        lenient().when(searchQueryBuilder.buildFilter(
            unitSearchOperation.getFilterSelections(),
            unitSearchOperation.getQueryFilter()
        )).thenReturn(ResolvedFilter.builder()
            .filter(unitSearchOperation.getQueryFilter())
            .references(List.of())
            .build());
        lenient().when(searchQueryBuilder.buildSorters(unitSearchOperation.getSorterSelections()))
            .thenReturn(ResolvedSorter.builder().build());
    }

    @Test
    void testSearch() {

        doThrow(JelasticException.class).when(elasticRepository).enumeratedSearch(any());
        doThrow(RuntimeException.class).when(elasticRepository).getElasticClient();

        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.search(unitSearchOperation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, exception.getErrorCode());
    }

    @Test
    void testElasticSearch() {
        doThrow(RuntimeException.class).when(elasticRepository).getElasticClient();
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.elasticSearch(unitSearchOperation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, exception.getErrorCode());
    }

    @Test
    void testPaginatedElasticSearchIoException() {
        doThrow(RuntimeException.class).when(elasticRepository).getElasticClient();
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.paginatedElasticSearch(unitSearchOperation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, exception.getErrorCode());
    }

    @Test
    void testJelasticSearch() {
        doThrow(JelasticException.class).when(elasticRepository).enumeratedSearch(any());
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.jElasticSearch(unitSearchOperation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, exception.getErrorCode());
    }

    @Test
    void testEntitySave() {
        var operation = EntitySaveOperation.builder()
            .entityType(EntityType.UNIT)
            .indexName("ab")
            .documentId("cd")
            .value(Value
                .exact("abcd"))
            .routingKey("wxyz")
            .build();
        doThrow(JelasticException.class).when(elasticRepository).save(any());
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.entitySave(operation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_CREATE_ERROR, exception.getErrorCode());
    }

    @Test
    void testMultiGetByIds() {
        var operation = MultiGetOperation.<String>builder()
            .entityType(EntityType.UNIT)
            .indexName("ab")
            .documentIds(List.of("cd", "ef", "gh"))
            .entityClassType(String.class)
            .build();
        doThrow(JelasticException.class).when(elasticRepository).searchByIds(any());
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.multiGetByIds(operation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, exception.getErrorCode());
    }


    @Test
    void testEntityFieldUpdate() {
        var operation = EntityFieldsUpdateOperation.builder()
            .entityType(EntityType.UNIT)
            .documentId("ab")
            .entityFieldValueData(new HashMap<>())
            .indexName("cd")
            .retryCount(1)
            .routingKey("wxyz")
            .build();
        doThrow(JelasticException.class).when(elasticRepository).updateField(any());
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.entityFieldsUpdate(operation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_UPDATE_ERROR, exception.getErrorCode());
    }


    @Test
    void testEntityUpdate() {
        var operation = EntityUpdateOperation.builder()
            .entityType(EntityType.UNIT)
            .indexName("ab")
            .documentId("cd")
            .value(Value.exact("xyz"))
            .routingKey("wxyz")
            .build();
        doThrow(JelasticException.class).when(elasticRepository).update(any());
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.entityUpdate(operation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_UPDATE_ERROR, exception.getErrorCode());
    }

    @Test
    void testEntityGet() {
        var operation = EntityGetOperation.<String>builder()
            .entityType(EntityType.UNIT)
            .indexName("ab")
            .documentId("cd")
            .routingKey("wxyz")
            .entityClassType(String.class)
            .build();
        doThrow(JelasticException.class).when(elasticRepository).get(any());
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.entityGet(operation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_FETCH_ERROR, exception.getErrorCode());
    }

    @Test
    void testEntityDelete() {
        var operation = EntityDeleteOperation.builder()
            .entityType(EntityType.UNIT)
            .documentId("ab")
            .indexName("cd")
            .routingKey("wxyz")
            .build();
        doThrow(JelasticException.class).when(elasticRepository).delete(any());
        var exception = assertThrows(ColumbusException.class, () -> unitEntityCommands.entityDelete(operation));
        assertEquals(ColumbusCoreErrorCode.ENTITY_DELETE_ERROR, exception.getErrorCode());
    }


    private UnitSearchOperation<TestUnit> getUnitSearchOperation() {
        return UnitSearchOperation.<TestUnit>builder()
            .entityType(EntityType.UNIT)
            .indexName("ab")
            .valueClassType(TestUnit.class)
            .filterSelection(FilterSelection.builder()
                .filterName("#=name")
                .value(Value.exact("cd"))
                .build())
            .queryFilter(EqualsFilter.builder()
                .field("carName")
                .value(Value.exact("cd"))
                .build())
            .pageNumber(0)
            .pageSize(1000)
            .build();
    }


}