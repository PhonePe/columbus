package com.phonepe.platform.columbus.core.models.forest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.models.core.dag.Edge;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public abstract class Forest {
    @JsonIgnore
    private final Map<String, Vertex> vertices = new HashMap<>();

    protected Forest(Map<String, List<String>> adjacencyGraph) {
        vertices.putAll(createVertices(adjacencyGraph));
        populateEdges(adjacencyGraph);
    }

    protected Forest(List<String> vertices, List<Edge> edges) {
        this(createAdjacencyGraph(vertices, edges));
    }

    public void addEdge(Edge edge) {
        addEdge(getVertex(edge.getSource()), getVertex(edge.getDestination()));
    }

    public void addEdges(Collection<Edge> edges) {
        edges.forEach(this::addEdge);
    }

    protected void addEdge(Vertex source, Vertex destination) {
        if (destination.hasPathTo(source)) {
            throw ColumbusException.error(ColumbusCoreErrorCode.CYCLIC_DEPENDENCY,
                "Edge '%s'-'%s' creates a cycle".formatted(source.getId(), destination.getId()));
        }
        source.connectTo(destination);
    }

    protected Vertex getVertex(String vertexId) {
        if (!vertices.containsKey(vertexId)) {
            throw ColumbusException.error(ColumbusCoreErrorCode.NO_NODE_FOUND,
                "Invalid vertex id '%s'".formatted(vertexId));
        }
        return vertices.get(vertexId);
    }

    protected List<Vertex> getSubtreeWithRoot(String vertexId) {
        Set<Vertex> explored = new HashSet<>();
        var pending = new ArrayDeque<Vertex>();
        pending.push(getVertex(vertexId));
        while (!pending.isEmpty()) {
            Vertex vertex = pending.pop();
            if (explored.add(vertex)) {
                pending.addAll(vertex.getChildren());
            }
        }
        return List.copyOf(explored);
    }

    private static Map<String, List<String>> createAdjacencyGraph(List<String> vertices, List<Edge> edges) {
        var adjacencyGraph = edges.stream().collect(Collectors.groupingBy(
            Edge::getSource,
            Collectors.mapping(Edge::getSource, Collectors.toList())
        ));
        vertices.forEach(vertex -> adjacencyGraph.putIfAbsent(vertex, List.of()));
        return adjacencyGraph;
    }

    private Map<String, Vertex> createVertices(Map<String, List<String>> adjacencyGraph) {
        return adjacencyGraph.keySet().stream().collect(Collectors.toMap(Function.identity(), Vertex::new));
    }

    private void populateEdges(Map<String, List<String>> adjacencyGraph) {
        var inDegree = adjacencyGraph.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var open = adjacencyGraph.keySet().stream()
            .filter(vertex -> inDegree.getOrDefault(vertex, 0L) == 0)
            .collect(Collectors.toCollection(ArrayDeque::new));

        // |V| iterations ensure each vertex is visited, except if there is a cycle
        for (int i = 0; i < vertices.size(); i++) {
            if (open.isEmpty()) {
                throw ColumbusException.error(ColumbusCoreErrorCode.CYCLIC_DEPENDENCY, "DAG contains a cycle");
            }
            var vertexId = open.pop();
            var vertex = vertices.get(vertexId);
            adjacencyGraph.get(vertexId).forEach(neighbor -> {
                vertex.connectTo(vertices.get(neighbor));
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    open.push(neighbor);
                }
            });
        }
    }
}
