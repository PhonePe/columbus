package com.phonepe.platform.columbus.core.models.forest;

import com.phonepe.platform.columbus.models.core.ColumbusTenantType;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryTree extends ColumbusTree<Category> {
    private ColumbusTenantType columbusTenantType;
    private long updatedAt;

    @Builder
    public CategoryTree(final ColumbusTenantType columbusTenantType, final List<Category> categories) {
        super(categories);
        this.columbusTenantType = columbusTenantType;
        this.updatedAt = categories.stream().mapToLong(Category::getUpdatedAt).max().orElse(0);
    }

    /**
     * A utility method to construct a ColumbusTree. Also does the validation.
     *
     * @return The categoryGraph associated with the given categories and the said namespace
     */
    public static CategoryTree build(ColumbusTenantType columbusTenantType, List<Category> categories) {
        CategoryTree categoryTree = CategoryTree.builder()
            .columbusTenantType(columbusTenantType)
            .categories(categories)
            .build();

        categoryTree.getNodes().values()
            .forEach(category -> category.setRoot(categoryTree.isRoot(category.getDocumentId())));

        return categoryTree;
    }

    /**
     * A function to get the Category given the id;
     */
    public Category getCategory(String categoryId) {
        return getNode(categoryId);
    }

    public String getParentNode(String categoryId) {
        return getParent(categoryId).getDocumentId();
    }

    public boolean createsCycle(Edge edge) {
        return getVertex(edge.getDestination()).hasPathTo(getVertex(edge.getSource()));
    }

    public boolean containsEdge(Edge edge) {
        return getVertex(edge.getSource()).hasEdgeTo(getVertex(edge.getDestination()));
    }

    public CategoryTree getFilteredTree(Predicate<Category> filter) {
        return CategoryTree.build(columbusTenantType,
            filteredSubtree(filter, category -> category.withOutgoingCategories(Collections.emptyList())));
    }
}
