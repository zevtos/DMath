package io.github.zevtos.dmathcore.domain.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Mutable adjacency-list based graph supporting directed and undirected modes.
 */
public class AdjacencyListGraph<V> implements Graph<V> {
    private final boolean directed;

    private final Map<V, Set<V>> adjacency;
    private int edgeCount;

    public AdjacencyListGraph(boolean directed) {
        this.directed = directed;
        this.adjacency = new HashMap<>();
        this.edgeCount = 0;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(adjacency.keySet());
    }

    @Override
    public boolean containsVertex(V vertex) {
        return adjacency.containsKey(vertex);
    }

    @Override
    public boolean addVertex(V vertex) {
        Objects.requireNonNull(vertex, "vertex");
        if (adjacency.containsKey(vertex)) {
            return false;
        }
        adjacency.put(vertex, new HashSet<>());
        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        Objects.requireNonNull(vertex, "vertex");
        Set<V> removed = adjacency.remove(vertex);
        if (removed == null) {
            return false;
        }
        // Decrease edges for all outgoing edges of the removed vertex.
        // For undirected graphs, each undirected edge was counted once at add time,
        // and appears exactly once in the removed set.
        int removedEdges = removed.size();
        edgeCount -= removedEdges;
        // Remove incoming edges from other vertices. For directed graphs, each incoming
        // edge contributes to edgeCount and must be decremented. For undirected graphs,
        // we already accounted for all incident edges above.
        for (Map.Entry<V, Set<V>> entry : adjacency.entrySet()) {
            if (entry.getValue().remove(vertex)) {
                if (directed) {
                    edgeCount--;
                }
            }
        }
        return true;
    }

    @Override
    public boolean addEdge(V from, V to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        addVertex(from);
        addVertex(to);
        boolean added = adjacency.get(from).add(to);
        if (!added) {
            return false;
        }
        edgeCount++;
        if (!directed) {
            // Mirror edge for undirected graphs (does not change edgeCount again)
            adjacency.get(to).add(from);
        }
        return true;
    }

    @Override
    public boolean removeEdge(V from, V to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Set<V> neighbors = adjacency.get(from);
        if (neighbors == null) {
            return false;
        }
        boolean removed = neighbors.remove(to);
        if (!removed) {
            return false;
        }
        edgeCount--;
        if (!directed) {
            Set<V> neighborsBack = adjacency.get(to);
            if (neighborsBack != null) {
                neighborsBack.remove(from);
            }
        }
        return true;
    }

    @Override
    public boolean hasEdge(V from, V to) {
        Set<V> neighbors = adjacency.get(from);
        return neighbors != null && neighbors.contains(to);
    }

    @Override
    public Set<V> getNeighbors(V vertex) {
        Set<V> neighbors = adjacency.get(vertex);
        if (neighbors == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(neighbors);
    }

    @Override
    public int getVertexCount() {
        return adjacency.size();
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }

    @Override
    public int inDegree(V vertex) {
        if (!directed) {
            return degree(vertex);
        }
        int count = 0;
        for (Set<V> neighbors : adjacency.values()) {
            if (neighbors.contains(vertex)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int outDegree(V vertex) {
        return degree(vertex);
    }
}
