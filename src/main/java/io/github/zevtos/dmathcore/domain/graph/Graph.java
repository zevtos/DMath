package io.github.zevtos.dmathcore.domain.graph;

import java.util.Collections;
import java.util.Set;

/**
 * A minimal, extensible graph interface supporting directed and undirected graphs.
 *
 * <p>Implementations should be mutable unless stated otherwise. For directed graphs,
 * {@link #getNeighbors(Object)} returns out-neighbors. For undirected graphs, it returns all neighbors.</p>
 */
public interface Graph<V> {
    /**
     * Indicates whether this graph is directed.
     */
    boolean isDirected();

    /**
     * Returns an unmodifiable view of vertices present in the graph.
     */
    Set<V> getVertices();

    /**
     * Returns {@code true} if the graph contains the specified vertex.
     */
    boolean containsVertex(V vertex);

    /**
     * Adds a vertex to the graph if not already present.
     *
     * @return true if the vertex was added, false if it was already present
     */
    boolean addVertex(V vertex);

    /**
     * Removes a vertex and all incident edges if present.
     *
     * @return true if the vertex was removed, false if it was not present
     */
    boolean removeVertex(V vertex);

    /**
     * Adds an edge between {@code from} and {@code to}. For undirected graphs this
     * creates a single undirected edge; for directed graphs an edge from {@code from} to {@code to}.
     * Self-loops are allowed.
     *
     * @return true if the edge was added, false if it already existed
     */
    boolean addEdge(V from, V to);

    /**
     * Removes an edge between {@code from} and {@code to}. For undirected graphs this
     * removes the undirected edge; for directed graphs the edge from {@code from} to {@code to}.
     *
     * @return true if the edge was removed, false if it did not exist
     */
    boolean removeEdge(V from, V to);

    /**
     * Returns {@code true} if an edge exists between {@code from} and {@code to}.
     */
    boolean hasEdge(V from, V to);

    /**
     * Returns an unmodifiable view of neighbors of {@code vertex}.
     * For directed graphs, returns out-neighbors.
     */
    Set<V> getNeighbors(V vertex);

    /**
     * Returns the number of vertices in the graph.
     */
    int getVertexCount();

    /**
     * Returns the number of edges. For undirected graphs, an undirected edge counts as one.
     */
    int getEdgeCount();

    /**
     * Returns the degree of a vertex. For directed graphs, this is the out-degree.
     */
    default int degree(V vertex) {
        return getNeighbors(vertex).size();
    }

    /**
     * Returns the in-degree of a vertex for directed graphs; for undirected graphs equals {@link #degree(Object)}.
     */
    default int inDegree(V vertex) {
        return isDirected() ? 0 : degree(vertex);
    }

    /**
     * Returns the out-degree of a vertex for directed graphs; for undirected graphs equals {@link #degree(Object)}.
     */
    default int outDegree(V vertex) {
        return degree(vertex);
    }

    /**
     * Returns an unmodifiable empty graph instance. Useful for fallbacks.
     */
    static <T> Graph<T> empty(boolean directed) {
        return new Graph<>() {
            @Override
            public boolean isDirected() { return directed; }
            @Override
            public Set<T> getVertices() { return Collections.emptySet(); }
            @Override
            public boolean containsVertex(T vertex) { return false; }
            @Override
            public boolean addVertex(T vertex) { return false; }
            @Override
            public boolean removeVertex(T vertex) { return false; }
            @Override
            public boolean addEdge(T from, T to) { return false; }
            @Override
            public boolean removeEdge(T from, T to) { return false; }
            @Override
            public boolean hasEdge(T from, T to) { return false; }
            @Override
            public Set<T> getNeighbors(T vertex) { return Collections.emptySet(); }
            @Override
            public int getVertexCount() { return 0; }
            @Override
            public int getEdgeCount() { return 0; }
        };
    }
}
