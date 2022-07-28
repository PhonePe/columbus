package com.phonepe.platform.columbus.core.commands;

import com.grookage.jelastic.core.models.helper.MapElement;
import com.grookage.jelastic.core.models.mapping.CreateMappingRequest;
import com.grookage.jelastic.core.models.template.CreateTemplateRequest;
import com.grookage.jelastic.core.repository.ElasticRepository;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.operations.CreateIndexOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateMappingOperation;
import com.phonepe.platform.columbus.core.models.operations.CreateTemplateOperation;
import com.phonepe.platform.columbus.core.models.operations.UpdateMappingOperation;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;

@Slf4j
@Getter
@SuperBuilder
@AllArgsConstructor
public abstract class IndexCommands implements ColumbusCommands {
    private final ElasticRepository elasticRepository;

    public void createIndexTemplate(final CreateTemplateOperation createTemplateOperation) {
        try {
            MapElement mapElement = new MapElement();
            mapElement.putAll(createTemplateOperation.getMappings());

            MapElement analysis = new MapElement();
            mapElement.putAll(createTemplateOperation.getAnalysis());
            CreateTemplateRequest createTemplateRequest = CreateTemplateRequest.builder()
                .templateName(createTemplateOperation.getTemplateName())
                .indexPattern(createTemplateOperation.getIndexPattern())
                .indexProperties(createTemplateOperation.getIndexProperties())
                .analysis(analysis)
                .mappingSource(mapElement)
                .build();

            this.getElasticRepository().createTemplate(createTemplateRequest);
        } catch (Exception ex) {
            log.error("Error while creating template for {}", createTemplateOperation.getTemplateName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.TEMPLATE_CREATE_ERROR, ex);
        }
    }

    public void createMapping(final CreateMappingOperation createMappingOperation) {
        try {
            MapElement mapElement = new MapElement();
            mapElement.putAll(createMappingOperation.getMappings());
            CreateMappingRequest createMappingRequest = CreateMappingRequest.builder()
                .indexName(createMappingOperation.getIndexName())
                .mappingSource(mapElement)
                .build();


            this.getElasticRepository().createMapping(createMappingRequest);
        } catch (Exception ex) {
            log.error("Error while creating mapping for {}", createMappingOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.MAPPING_CREATE_ERROR, ex);
        }
    }

    public void updateMapping(final UpdateMappingOperation updateMappingOperation) {
        try {
            MapElement mapElement = new MapElement();
            mapElement.putAll(updateMappingOperation.getMappings());
            CreateMappingRequest createMappingRequest = CreateMappingRequest.builder()
                .indexName(updateMappingOperation.getIndexName())
                .mappingSource(mapElement)
                .build();


            this.getElasticRepository().createMapping(createMappingRequest);
        } catch (Exception ex) {
            log.error("Error while updating mapping for {}", updateMappingOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.MAPPING_UPDATE_ERROR, ex);
        }
    }

    public void createIndex(final CreateIndexOperation createIndexOperation) {
        try {
            if (existsIndex(createIndexOperation.getIndexName())) {
                throw ColumbusException.error(ColumbusCoreErrorCode.INDEX_CREATE_ERROR, "Index already exists");
            }
            this.getElasticRepository().createIndex(createIndexOperation.getIndexName());
        } catch (Exception ex) {
            log.error("Error while creating index for {}", createIndexOperation.getIndexName(), ex);
            throw ColumbusException.propagate(ColumbusCoreErrorCode.INDEX_CREATE_ERROR, ex);
        }
    }

    public boolean existsIndex(final String indexName) {
        try {
            return this.getElasticRepository().getElasticClient().getClient()
                .indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (Exception ex) {
            log.error("Error while checking index exists for {}", indexName, ex);
            throw ColumbusException.propagate(ColumbusErrorCode.INTERNAL_ERROR, ex);
        }
    }
}
