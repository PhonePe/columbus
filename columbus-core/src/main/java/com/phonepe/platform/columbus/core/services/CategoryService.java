package com.phonepe.platform.columbus.core.services;

import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.DeleteCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.InitialiseServiceRequest;
import com.phonepe.platform.columbus.models.core.request.category.SearchCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryFieldsRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryLinkRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateMappingServiceRequest;

public interface CategoryService extends
    ColumbusIndexService<InitialiseServiceRequest, UpdateMappingServiceRequest, Boolean>,
    ColumbusEntityService<CreateCategoryRequest, SearchCategoryRequest, UpdateCategoryFieldsRequest, DeleteCategoryRequest, Category> {
    void unLink(UpdateCategoryLinkRequest categoryLinkUnlinkRequest);

    void link(UpdateCategoryLinkRequest categoryLinkUnlinkRequest);
}
