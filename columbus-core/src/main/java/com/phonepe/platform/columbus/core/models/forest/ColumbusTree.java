package com.phonepe.platform.columbus.core.models.forest;

import com.phonepe.platform.columbus.models.core.dag.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class ColumbusTree<T extends Node> extends Forest {
    private final Map<String, T> nodes = new HashMap<>();

    protected ColumbusTree(List<T> nodes) {
        super(nodes.stream().collect(Collectors.toMap(
            Node::getId, Node::getChildren)));
        this.nodes.putAll(nodes.stream().collect(Collectors.toMap(
            Node::getId, Function.identity())));
    }

    public T getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    public T getParent(String nodeId) {
        return getVertex(nodeId)
            .getParents()
            .stream()
            .findFirst()
            .map(Vertex::getId)
            .map(nodes::get)
            .orElse(null);
    }

    public boolean isRoot(String nodeId) {
        return getVertex(nodeId).isRoot();
    }

    @SuppressWarnings("java:S3958") // SonarQube fix
    public List<T> getSubtree(String nodeId) {
        return getSubtreeWithRoot(nodeId).stream()
            .map(Vertex::getId)
            .map(nodes::get)
            .toList();
    }

    protected List<T> filteredSubtree(Predicate<T> filter, UnaryOperator<T> cloner) {
        var clonedNodes = new HashMap<String, T>();
        getVertices().values().stream()
            .filter(Vertex::isRoot)
            .forEach(vertex -> dfsFilter(vertex, filter, cloner, clonedNodes));
        return new ArrayList<>(clonedNodes.values());
    }

    @SuppressWarnings("java:S3958") // SonarQube fix
    private Optional<String> dfsFilter(Vertex vertex, Predicate<T> filter, UnaryOperator<T> cloner, HashMap<String, T> clonedNodes) {
        String id = vertex.getId();
        if (clonedNodes.containsKey(id)) {
            return Optional.of(id);
        }
        var node = getNode(id);
        if (!filter.test(node)) {
            return Optional.empty();
        }
        var clone = cloner.apply(node);
        clonedNodes.put(id, clone);
        var children = vertex.getChildren().stream()
            .map(child -> dfsFilter(child, filter, cloner, clonedNodes))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        clone.setChildren(children);
        return Optional.of(id);
    }
}
