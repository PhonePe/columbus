package com.phonepe.platform.columbus.core.commands.category;

import com.phonepe.platform.columbus.core.commands.EntityCommands;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.models.EntityType;
import com.phonepe.platform.columbus.core.models.forest.CategoryTree;
import com.phonepe.platform.columbus.core.models.operations.EntitiesSearchOperation;
import com.phonepe.platform.columbus.core.models.operations.EntityGetOperation;
import com.phonepe.platform.columbus.core.utils.CategoryTransformUtils;
import com.phonepe.platform.columbus.core.utils.IndexUtils;
import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.EntityStatus;
import com.phonepe.platform.columbus.models.core.category.Platform;
import com.phonepe.platform.columbus.models.core.category.StoredCategory;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Getter
@SuperBuilder
@SuppressWarnings("java:S3958") // SonarQube fix
@EqualsAndHashCode(callSuper = true)
public class CategoryEntityCommands extends EntityCommands {
    private EntitiesSearchOperation<StoredCategory> categorySearchOperation(String indexName) {
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
                    .values(Value.exact(Arrays.stream(EntityStatus.values())
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

    private Optional<List<Category>> getAll(final ColumbusTenantType columbusTenantType) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, columbusTenantType.name());

        List<Category> categoryEntities = search(categorySearchOperation(indexName)).stream()
            .map(CategoryTransformUtils::toModel)
            .toList();

        CategoryTransformUtils.setOutgoingCategories(categoryEntities);

        if (categoryEntities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(categoryEntities);
    }

    public CategoryTree getTree(final ColumbusTenantType columbusTenantType, final List<Platform> supportedPlatforms) {
        return getTreeIfExists(columbusTenantType, supportedPlatforms)
            .orElseThrow(() -> ColumbusException.error(ColumbusCoreErrorCode.RECORD_NOT_FOUND,
                "Tenant records not found"));
    }

    public CategoryTree getTreeForCache(final ColumbusTenantType columbusTenantType, final List<Platform> supportedPlatforms) {
        return getTreeIfExists(columbusTenantType, supportedPlatforms)
            .orElse(CategoryTree.build(columbusTenantType, List.of()));
    }

    public Optional<Category> getById(ColumbusTenantType columbusTenantType, String documentId) {
        String indexName = IndexUtils.getIndexName(EntityType.CATEGORY, columbusTenantType.name());
        Optional<StoredCategory> categoryStorageEntity = entityGet(EntityGetOperation.<StoredCategory>builder()
            .entityType(EntityType.CATEGORY)
            .indexName(indexName)
            .documentId(documentId)
            .entityClassType(StoredCategory.class)
            .build());

        return categoryStorageEntity.map(CategoryTransformUtils::toModel);

    }

    private Optional<CategoryTree> getTreeIfExists(ColumbusTenantType columbusTenantType, List<Platform> supportedPlatforms) {
        return getAll(columbusTenantType)
            .map(categoryEntities -> categoryEntities.stream()
                .filter(category -> Objects.isNull(category.getSupportedPlatforms()) ||
                    category.getSupportedPlatforms().stream().anyMatch(supportedPlatforms::contains))
                .toList())
            .map(categories -> CategoryTree.build(columbusTenantType, categories));
    }
}
