package com.phonepe.platform.columbus.core.commands.unit;

import com.grookage.jelastic.core.exception.JelasticException;
import com.grookage.jelastic.core.repository.ElasticRepository;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.operations.CreateMappingOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.models.operations.UpdateMappingOperation;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UnitIndexManageCommandsTest {
    UnitIndexManageCommands unitIndexManageCommands;
    @Mock
    ElasticRepository elasticRepository;

    @BeforeEach
    void setup() {
        unitIndexManageCommands = UnitIndexManageCommands.builder()
            .elasticRepository(elasticRepository)
            .build();
    }

    @Test
    void testCreateIndexTemplate() {
        CreateTemplateOperation createTemplateOperation = CreateTemplateOperation.builder()
            .entityType(EntityType.UNIT)
            .templateName("ab")
            .indexName("cd")
            .indexPattern("wxyz")
            .analysis(new HashMap<>())
            .mappings(new HashMap<>())
            .build();
        doThrow(JelasticException.class).when(elasticRepository).createTemplate(any());
        var exception = assertThrows(ColumbusException.class, () -> unitIndexManageCommands.createIndexTemplate(createTemplateOperation));
        assertEquals(ColumbusCoreErrorCode.TEMPLATE_CREATE_ERROR, exception.getErrorCode());
    }

    @Test
    void testCreateMapping() {
        CreateMappingOperation createMappingOperation = CreateMappingOperation.builder()
            .entityType(EntityType.UNIT)
            .mappings(new HashMap<>()).indexName("ab")
            .build();
        doThrow(JelasticException.class).when(elasticRepository).createMapping(any());
        var exception = assertThrows(ColumbusException.class, () -> unitIndexManageCommands.createMapping(createMappingOperation));
        assertEquals(ColumbusCoreErrorCode.MAPPING_CREATE_ERROR, exception.getErrorCode());
    }

    @Test
    void testUpdateMapping() {
        UpdateMappingOperation updateMappingOperation = UpdateMappingOperation.builder()
            .entityType(EntityType.UNIT).indexName("ab")
            .mappings(new HashMap<>())
            .build();
        doThrow(JelasticException.class).when(elasticRepository).createMapping(any());
        var exception = assertThrows(ColumbusException.class, () -> unitIndexManageCommands.updateMapping(updateMappingOperation));
        assertEquals(ColumbusCoreErrorCode.MAPPING_UPDATE_ERROR, exception.getErrorCode());
    }
}