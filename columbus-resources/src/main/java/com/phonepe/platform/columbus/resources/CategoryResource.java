package com.phonepe.platform.columbus.resources;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.phonepe.models.response.GenericResponse;
import com.phonepe.platform.columbus.core.services.CategoryService;
import com.phonepe.platform.columbus.core.utils.CategoryValidationUtils;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.DeleteCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.InitialiseServiceRequest;
import com.phonepe.platform.columbus.models.core.request.category.SearchCategoryRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryFieldsRequest;
import com.phonepe.platform.columbus.models.core.request.category.UpdateCategoryLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@Path("/v1/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
@Tag(name = "Api for category index and entity management")
@AllArgsConstructor
public class CategoryResource {
    private final CategoryService categoryService;
    private final List<ColumbusTenantType> validTenantTypes;

    @POST
    @Timed
    @ExceptionMetered
    @Path("/{tenantType}/initialise")
    @Operation(summary = "Create the mapped index")
    public void initialise(
        @Valid @NotNull InitialiseServiceRequest categoryInitialiseRequest,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.tenantValidation(
            tenantType,
            categoryInitialiseRequest.getTenantType(),
            validTenantTypes
        );
        categoryService.initialise(categoryInitialiseRequest);
    }

    @POST
    @Timed
    @ExceptionMetered
    @Path("/{tenantType}/link")
    @Operation(summary = "Create link between categories")
    public void link(
        @Valid @NotNull UpdateCategoryLinkRequest updateCategoryLinkRequest,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.tenantValidation(
            tenantType,
            updateCategoryLinkRequest.getTenantType(),
            validTenantTypes
        );
        categoryService.link(updateCategoryLinkRequest);
    }

    @POST
    @Timed
    @ExceptionMetered
    @Path("/{tenantType}/unlink")
    @Operation(summary = "Create the mapped index")
    public void unlink(
        @Valid @NotNull UpdateCategoryLinkRequest updateCategoryLinkRequest,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.tenantValidation(
            tenantType,
            updateCategoryLinkRequest.getTenantType(),
            validTenantTypes
        );
        categoryService.unLink(updateCategoryLinkRequest);
    }


    @POST
    @Timed
    @ExceptionMetered
    @Path("/{tenantType}/entity")
    @Operation(summary = "Create category entity")
    public void createEntity(
        @Valid @NotNull CreateCategoryRequest createCategoryRequest,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.tenantValidation(
            tenantType,
            createCategoryRequest.getTenantType(),
            validTenantTypes
        );
        categoryService.create(createCategoryRequest);
    }

    @Timed
    @DELETE
    @ExceptionMetered
    @Path("/{tenantType}/entity")
    @Operation(summary = "Delete category entity")
    public void deleteEntity(
        @Valid @NotNull DeleteCategoryRequest deleteCategoryRequest,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.tenantValidation(
            tenantType,
            deleteCategoryRequest.getTenantType(),
            validTenantTypes
        );
        categoryService.delete(deleteCategoryRequest);
    }

    @PUT
    @Timed
    @ExceptionMetered
    @Path("/{tenantType}/entity")
    @Operation(summary = "Update entity")
    public void updateEntity(
        @Valid @NotNull UpdateCategoryFieldsRequest updateCategoryFieldsRequest,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.tenantValidation(
            tenantType,
            updateCategoryFieldsRequest.getTenantType(),
            validTenantTypes
        );
        categoryService.update(updateCategoryFieldsRequest);
    }

    @POST
    @Timed
    @ExceptionMetered
    @Path("/{tenantType}/entity/search")
    @Operation(summary = "Get multiple category entities")
    public GenericResponse<List<Category>> multiGet(
        @Valid @NotNull SearchCategoryRequest searchCategoryRequest,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.tenantValidation(
            tenantType,
            searchCategoryRequest.getTenantType(),
            validTenantTypes
        );
        List<Category> categoryEntities = categoryService.search(searchCategoryRequest);
        return GenericResponse.<List<Category>>builder()
            .success(!categoryEntities.isEmpty())
            .data(categoryEntities)
            .build();
    }

    @GET
    @Timed
    @ExceptionMetered
    @Path("/{tenantType}/entity/{entityId}")
    @Operation(summary = "Get category entity")
    public GenericResponse<Category> getEntity(
        @NotNull @PathParam("entityId") String documentId,
        @Parameter(schema = @Schema(type = "string")) @NotNull @PathParam("tenantType") ColumbusTenantType tenantType
    ) {
        CategoryValidationUtils.validateTenantType(tenantType, validTenantTypes);
        Category categoryEntity = categoryService.get(SearchCategoryRequest.builder()
            .documentIds(List.of(documentId))
            .tenantType(tenantType)
            .build());
        return GenericResponse.<Category>builder()
            .success(categoryEntity != null)
            .data(categoryEntity)
            .build();
    }
}
