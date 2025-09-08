package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.AdjacencyListGraph;
import io.github.zevtos.dmathcore.domain.graph.Graph;
import io.github.zevtos.dmathcore.domain.graph.GraphAlgorithms;

import java.util.*;

/**
 * Legacy-compatible int graph API that adapts to {@link Graph<Integer>}.
 * Provides familiar methods from the prior implementation while delegating to a new backing graph.
 */
public class LegacyIntGraphAdapter {
    private final Graph<Integer> delegate;

    public LegacyIntGraphAdapter(int numVertices) {
        this(false, numVertices);
    }

    public LegacyIntGraphAdapter(boolean directed, int numVertices) {
        Graph<Integer> g = new AdjacencyListGraph<>(directed);
        for (int i = 0; i < numVertices; i++) {
            g.addVertex(i);
        }
        this.delegate = g;
    }

    public LegacyIntGraphAdapter(Set<Integer>[] adjacency) {
        this(false, adjacency);
    }

    public LegacyIntGraphAdapter(boolean directed, Set<Integer>[] adjacency) {
        Graph<Integer> g = new AdjacencyListGraph<>(directed);
        for (int i = 0; i < adjacency.length; i++) {
            g.addVertex(i);
        }
        for (int i = 0; i < adjacency.length; i++) {
            for (Integer n : adjacency[i]) {
                g.addEdge(i, n);
            }
        }
        this.delegate = g;
    }

    public LegacyIntGraphAdapter(Graph<Integer> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    public int getNumVertices() {
        return delegate.getVertexCount();
    }

    public boolean isDirected() { return delegate.isDirected(); }

    public void addEdge(int a, int b) {
        ensureVerticesPresent(a, b);
        delegate.addEdge(a, b);
    }

    public void removeEdge(int a, int b) {
        if (!delegate.containsVertex(a) || !delegate.containsVertex(b)) return;
        delegate.removeEdge(a, b);
    }

    public boolean hasEdge(int a, int b) {
        return delegate.hasEdge(a, b);
    }

    public Set<Integer> getNeighbors(int vertex) {
        return delegate.getNeighbors(vertex);
    }

    public int getDegree(int vertex) {
        return delegate.degree(vertex);
    }

    public int getHighDegreeVertex() {
        int best = -1;
        int max = -1;
        for (Integer v : new TreeSet<>(delegate.getVertices())) {
            int d = delegate.degree(v);
            if (d > max) {
                max = d;
                best = v;
            }
        }
        return best;
    }

    public void removeVertex(int vertex) {
        delegate.removeVertex(vertex);
    }

    public boolean isConnected() {
        return GraphAlgorithms.isConnected(delegate);
    }

    public List<Set<Integer>> getConnectedComponents() {
        return GraphAlgorithms.connectedComponents(delegate);
    }

    public List<Integer> shortestPath(int start, int end) {
        return GraphAlgorithms.shortestPathBfs(delegate, start, end);
    }

    public boolean hasBridges() {
        if (delegate.isDirected()) return false; // maintain old behavior gap by returning false for directed
        return GraphAlgorithms.hasBridgesUndirected(delegate);
    }

    public void addLoop(int vertex) {
        ensureVerticesPresent(vertex);
        delegate.addEdge(vertex, vertex);
    }

    public boolean hasLoop(int vertex) {
        return delegate.hasEdge(vertex, vertex);
    }

    public Set<Integer>[] getAdj() {
        @SuppressWarnings("unchecked")
        Set<Integer>[] arr = new HashSet[delegate.getVertexCount()];
        for (int i = 0; i < delegate.getVertexCount(); i++) {
            Set<Integer> neighbors = delegate.getNeighbors(i);
            arr[i] = new HashSet<>(neighbors);
        }
        return arr;
    }

    public void printGraph() {
        for (Integer v : new TreeSet<>(delegate.getVertices())) {
            System.out.println(v + ": " + delegate.getNeighbors(v));
        }
    }

    public void addVertex(int vertex) {
        delegate.addVertex(vertex);
    }

    public boolean isValidVertex(int vertex) {
        return vertex >= 0 && vertex < delegate.getVertexCount();
    }

    public static Set<Integer>[] createCompleteGraph(int numVertices) {
        @SuppressWarnings("unchecked")
        Set<Integer>[] newGraph = new HashSet[numVertices];
        for (int i = 0; i < numVertices; i++) {
            newGraph[i] = new HashSet<>();
            for (int j = 0; j < numVertices; j++) {
                if (i != j) {
                    newGraph[i].add(j);
                }
            }
        }
        return newGraph;
    }

    public ArrayList<Integer> subtraction(ArrayList<Integer> list, int vert) {
        ArrayList<Integer> newList = new ArrayList<>(list);
        newList.removeAll(getNeighbors(vert));
        return newList;
    }

    public Set<Integer>[] getSubgraph(Set<Integer> vertices) {
        @SuppressWarnings("unchecked")
        Set<Integer>[] subgraph = new HashSet[vertices.size()];
        List<Integer> indexMap = new ArrayList<>(vertices);
        for (int i = 0; i < indexMap.size(); i++) {
            int vertex = indexMap.get(i);
            subgraph[i] = new HashSet<>();
            for (int neighbor : delegate.getNeighbors(vertex)) {
                if (vertices.contains(neighbor)) {
                    subgraph[i].add(neighbor);
                }
            }
        }
        return subgraph;
    }

    private void ensureVerticesPresent(int... vertices) {
        for (int v : vertices) delegate.addVertex(v);
    }
}
