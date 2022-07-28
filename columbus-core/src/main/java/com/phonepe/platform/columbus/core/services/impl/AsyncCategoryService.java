package com.phonepe.platform.columbus.core.services.impl;

import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.core.worker.producer.ColumbusCategoryProducerRegistry;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.DeleteCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.InitialiseServiceRequest;
import com.phonepe.platform.columbus.models.core.request.category.SearchCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryFieldsRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryLinkRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateMappingServiceRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Singleton
@AllArgsConstructor
public class AsyncCategoryService implements CategoryService {
    private final CategoryService syncCategoryService;
    private final ColumbusCategoryProducerRegistry producer;

    @Override
    public void create(CreateCategoryRequest createRequest) {
        producer.publish(createRequest);
    }

    @Override
    public Category get(SearchCategoryRequest searchRequest) {
        return syncCategoryService.get(searchRequest);
    }

    @Override
    public List<Category> search(SearchCategoryRequest searchRequest) {
        return syncCategoryService.search(searchRequest);
    }

    @Override
    public void update(UpdateCategoryFieldsRequest categoryUpdateEntityFieldsRequest) {
        producer.publish(categoryUpdateEntityFieldsRequest);
    }

    @Override
    public void delete(DeleteCategoryRequest deleteRequest) {
        producer.publish(deleteRequest);
    }

    @Override
    public void initialise(InitialiseServiceRequest categoryInitialiseRequest) {
        producer.publish(categoryInitialiseRequest);
    }

    @Override
    public Boolean updateMapping(UpdateMappingServiceRequest updateRequest) {
        return syncCategoryService.updateMapping(updateRequest);
    }

    @Override
    public void unLink(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        syncCategoryService.unLink(categoryLinkUnlinkRequest);
    }

    @Override
    public void link(final UpdateCategoryLinkRequest categoryLinkUnlinkRequest) {
        syncCategoryService.link(categoryLinkUnlinkRequest);
    }
}
