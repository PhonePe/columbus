package com.phonepe.platform.columbus.core.models.forest;

import com.phonepe.platform.columbus.core.models.TestTenant;
import com.phonepe.platform.columbus.models.core.category.Category;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryTreeTest {
    @Test
    void testTree() {
        ArrayList<Category> objects = new ArrayList<>();

        ArrayList<String> objects1 = new ArrayList<>();
        objects1.add("GS");
        objects1.add("MS");

        Category categoryEntity = Category.builder()
            .documentId("WR")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(objects1)
            .build();

        objects.add(categoryEntity);

        Category categoryEntity1 = Category.builder()
            .documentId("GS")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(List.of())
            .build();

        objects.add(categoryEntity1);

        Category categoryEntity2 = Category.builder()
            .documentId("MS")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(List.of())
            .build();

        objects.add(categoryEntity2);

        Category categoryEntity3 = Category.builder()
            .documentId("MSS")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(objects1)
            .build();

        objects.add(categoryEntity3);

        CategoryTree categoryTree = CategoryTree.build(TestTenant.TEST_TENANT, objects);
        assertEquals(4, categoryTree.getNodes().size());
    }

    @Test
    void testAddEdge() {
        ArrayList<Category> objects = new ArrayList<>();

        ArrayList<String> objects1 = new ArrayList<>();
        objects1.add("GS");
        objects1.add("MS");

        Category categoryEntity = Category.builder()
            .documentId("WR")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(objects1)
            .build();

        objects.add(categoryEntity);

        Category categoryEntity1 = Category.builder()
            .documentId("GS")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(List.of())
            .build();

        objects.add(categoryEntity1);

        Category categoryEntity2 = Category.builder()
            .documentId("MS")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(List.of())
            .build();

        objects.add(categoryEntity2);

        Category categoryEntity3 = Category.builder()
            .documentId("MSS")
            .displayName("TES")
            .name("TEST")
            .outgoingCategories(objects1)
            .build();

        objects.add(categoryEntity3);

        CategoryTree categoryTree = CategoryTree.build(TestTenant.TEST_TENANT, objects);

        Set<Edge> objectSet = new HashSet<>();
        objectSet.add(new Edge("WR", "MSS"));
        categoryTree.addEdges(objectSet);

        assertEquals(3, categoryTree.getVertex("WR").getChildren().size());
    }

    @Test
    void testGetSubtree() {
        CategoryTree tree = createCategoryTree();
        List<String> expected = List.of("cat6", "cat7", "cat8");

        List<Category> subtree = tree.getSubtree("cat6");

        assertEquals(expected.size(), subtree.size());
        List<String> nodes = subtree.stream().map(Category::getId).sorted().toList();
        assertEquals(expected, nodes);
    }

    @Test
    void testGetFilteredSubtree() {
        CategoryTree tree = createCategoryTree();
        List<String> expected = List.of("cat1", "cat3", "cat5");

        CategoryTree filteredTree = tree.getFilteredTree(category -> Integer.parseInt(category.getDocumentId().substring(3)) % 2 == 1);

        assertEquals(expected.size(), filteredTree.getNodes().size());
        List<String> nodes = filteredTree.getNodes().keySet().stream().sorted().toList();
        assertEquals(expected, nodes);
        assertEquals(List.of("cat3"), filteredTree.getNode("cat1").getOutgoingCategories());
        assertTrue(filteredTree.getNode("cat5").getOutgoingCategories().isEmpty());
    }

    private CategoryTree createCategoryTree() {
        return CategoryTree.build(TestTenant.TEST_TENANT, List.of(
            createCategory("cat1", List.of("cat2", "cat3")),
            createCategory("cat2", List.of("cat4")),
            createCategory("cat3", List.of()),
            createCategory("cat4", List.of()),
            createCategory("cat5", List.of("cat6")),
            createCategory("cat6", List.of("cat7", "cat8")),
            createCategory("cat7", List.of()),
            createCategory("cat8", List.of())
        ));
    }

    private Category createCategory(String id, List<String> children) {
        return Category.builder()
            .documentId(id)
            .name(id)
            .displayName(id)
            .outgoingCategories(children)
            .build();
    }
}