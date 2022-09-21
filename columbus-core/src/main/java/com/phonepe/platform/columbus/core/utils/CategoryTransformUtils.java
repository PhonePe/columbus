package com.phonepe.platform.columbus.core.utils;

import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.category.StoredCategory;
import com.phonepe.platform.columbus.models.core.request.category.CreateCategoryRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class CategoryTransformUtils {
    private final int DEFAULT_SCORE = 0;

    public Category toModel(StoredCategory categoryStorageEntity) {
        return Category.builder()
            .documentId(categoryStorageEntity.getDocumentId())
            .name(categoryStorageEntity.getName())
            .displayName(categoryStorageEntity.getDisplayName())
            .description(categoryStorageEntity.getDescription())
            .score(categoryStorageEntity.getScore())
            .status(categoryStorageEntity.getStatus())
            .outgoingCategories(toList(categoryStorageEntity.getOutgoingCategories()))
            .root(categoryStorageEntity.isRoot())
            .searchMeta(categoryStorageEntity.getSearchMeta())
            .extras(categoryStorageEntity.getExtras())
            .updatedAt(categoryStorageEntity.getUpdatedAt())
            .createdAt(categoryStorageEntity.getCreatedAt())
            .supportedPlatforms(categoryStorageEntity.getSupportedPlatforms())
            .mapped(categoryStorageEntity.isMapped())
            .build();
    }

    private <T> List<T> toList(List<T> list) {
        return list == null ? List.of() : list;
    }

    public StoredCategory toStorageEntity(CreateCategoryRequest categoryEntityInsertRequest) {
        return StoredCategory.builder()
            .documentId(categoryEntityInsertRequest.getId())
            .name(categoryEntityInsertRequest.getName())
            .displayName(categoryEntityInsertRequest.getDisplayName())
            .description(categoryEntityInsertRequest.getDescription())
            .score(DEFAULT_SCORE)
            .status(categoryEntityInsertRequest.getStatus())
            .outgoingCategories(List.of())
            .searchMeta(categoryEntityInsertRequest.getSearchMeta())
            .extras(categoryEntityInsertRequest.getExtras())
            .updatedAt(System.currentTimeMillis())
            .createdAt(System.currentTimeMillis())
            .supportedPlatforms(toList(categoryEntityInsertRequest.getSupportedPlatforms()))
            .createdBy(categoryEntityInsertRequest.getCreatedBy())
            .mapped(categoryEntityInsertRequest.isMapped())
            .build();
    }

    public void setOutgoingCategories(List<Category> categories) {
        var categoryMap = categories.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Category::getDocumentId, Function.identity()));

        categories.forEach(category -> category.setOutgoingCategories(filterOutgoingCategories(categoryMap, category)));
    }

    @SuppressWarnings("java:S3958") // SonarQube fix
    private List<String> filterOutgoingCategories(Map<String, Category> categoryMap, Category category) {
        return category.getOutgoingCategories()
            .stream()
            .filter(categoryMap::containsKey)
            .toList();
    }
}