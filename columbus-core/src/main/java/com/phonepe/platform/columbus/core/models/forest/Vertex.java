package com.phonepe.platform.columbus.core.models.forest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vertex implements Comparable<Vertex> {
    @ToString.Include
    @EqualsAndHashCode.Include
    private final String id;
    private final Set<Vertex> parents = new HashSet<>();
    private final Set<Vertex> children = new HashSet<>();

    public void connectTo(Vertex other) {
        children.add(other);
        other.parents.add(this);
    }

    public boolean hasEdgeTo(Vertex vertex) {
        return children.contains(vertex);
    }

    public boolean hasPathTo(Vertex vertex) {
        if (equals(vertex)) {
            return true;
        }
        if (hasEdgeTo(vertex)) {
            return true;
        }
        return vertex.parents.stream().anyMatch(this::hasPathTo);
    }

    public boolean isRoot() {
        return parents.isEmpty();
    }

    @Override
    public int compareTo(@NonNull Vertex other) {
        return id.compareTo(other.id);
    }
}
