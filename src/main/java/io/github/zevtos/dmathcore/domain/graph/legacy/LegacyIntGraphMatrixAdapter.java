package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.AdjacencyListGraph;
import io.github.zevtos.dmathcore.domain.graph.Graph;
import io.github.zevtos.dmathcore.domain.graph.GraphAlgorithms;

import java.util.*;

/**
 * Legacy-compatible matrix-based graph wrapper for int vertices [0..N-1].
 *
 * <p>This adapter maintains an adjacency matrix alongside a backing {@link Graph<Integer>} implementation
 * (undirected). It preserves behaviors from the previous GraphMatrix class while integrating with the
 * new graph API.</p>
 */
public class LegacyIntGraphMatrixAdapter {
    private final Graph<Integer> delegate;
    private int[][] adjacencyMatrix;

    public LegacyIntGraphMatrixAdapter(int numVertices) {
        this.adjacencyMatrix = new int[numVertices][numVertices];
        this.delegate = new AdjacencyListGraph<>(false);
        for (int i = 0; i < numVertices; i++) {
            delegate.addVertex(i);
        }
    }

    public LegacyIntGraphMatrixAdapter(Set<Integer>[] graph) {
        int n = graph.length;
        this.adjacencyMatrix = new int[n][n];
        this.delegate = new AdjacencyListGraph<>(false);
        for (int i = 0; i < n; i++) delegate.addVertex(i);
        for (int i = 0; i < n; i++) {
            for (int j : graph[i]) {
                adjacencyMatrix[i][j] = 1;
                adjacencyMatrix[j][i] = 1;
                delegate.addEdge(i, j);
            }
        }
    }

    public LegacyIntGraphMatrixAdapter(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix.length;
        this.adjacencyMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, this.adjacencyMatrix[i], 0, n);
        }
        this.delegate = new AdjacencyListGraph<>(false);
        for (int i = 0; i < n; i++) delegate.addVertex(i);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.adjacencyMatrix[i][j] > 0) {
                    delegate.addEdge(i, j);
                }
            }
        }
    }

    public int getNumVertices() {
        return adjacencyMatrix.length;
    }

    public boolean isValidVertex(int vertex) {
        return vertex >= 0 && vertex < adjacencyMatrix.length;
    }

    public void addEdge(int a, int b) {
        if (!isValidVertices(a, b)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        delegate.addEdge(a, b);
        adjacencyMatrix[a][b] = 1;
        adjacencyMatrix[b][a] = 1;
    }

    public void removeEdge(int a, int b) {
        if (!isValidVertices(a, b)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        delegate.removeEdge(a, b);
        adjacencyMatrix[a][b] = 0;
        adjacencyMatrix[b][a] = 0;
    }

    public boolean hasEdge(int a, int b) {
        if (!isValidVertices(a, b)) {
            return false;
        }
        return adjacencyMatrix[a][b] > 0;
    }

    public void removeVertex(int vertex) {
        if (!isValidVertex(vertex)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        int n = adjacencyMatrix.length;
        int[][] newAdj = new int[n - 1][n - 1];
        for (int i = 0, ni = 0; i < n; i++) {
            if (i == vertex) continue;
            for (int j = 0, nj = 0; j < n; j++) {
                if (j == vertex) continue;
                newAdj[ni][nj] = adjacencyMatrix[i][j];
                nj++;
            }
            ni++;
        }
        this.adjacencyMatrix = newAdj;
        Graph<Integer> rebuilt = new AdjacencyListGraph<>(false);
        for (int i = 0; i < newAdj.length; i++) rebuilt.addVertex(i);
        for (int i = 0; i < newAdj.length; i++) {
            for (int j = 0; j < newAdj.length; j++) {
                if (newAdj[i][j] > 0) rebuilt.addEdge(i, j);
            }
        }
        syncDelegateWithMatrix();
    }

    private void syncDelegateWithMatrix() {
        int n = adjacencyMatrix.length;
        for (int i = 0; i < n; i++) delegate.addVertex(i);
        for (int i = 0; i < n; i++) {
            for (Integer nb : new HashSet<>(delegate.getNeighbors(i))) {
                if (nb >= n || adjacencyMatrix[i][nb] == 0) {
                    delegate.removeEdge(i, nb);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] > 0 && !delegate.hasEdge(i, j)) {
                    delegate.addEdge(i, j);
                }
            }
        }
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void displayAdjacencyMatrix() {
        System.out.println("Матрица смежности:");
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                System.out.printf("%4s", adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String adjacencyMatrixToString() {
        StringBuilder text = new StringBuilder("Матрица смежности:");
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                text.append(String.format("%4s", adjacencyMatrix[i][j] + " "));
            }
            text.append("\n");
        }
        return text.toString();
    }

    public boolean hasPath(int source, int destination) {
        if (!isValidVertices(source, destination)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        boolean[] visited = new boolean[adjacencyMatrix.length];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(source);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (u == destination) return true;
            if (visited[u]) continue;
            visited[u] = true;
            for (int v = 0; v < adjacencyMatrix.length; v++) {
                if (adjacencyMatrix[u][v] > 0 && !visited[v]) stack.push(v);
            }
        }
        return false;
    }

    public void addLoop(int vertex) {
        if (!isValidVertex(vertex)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        delegate.addEdge(vertex, vertex);
        adjacencyMatrix[vertex][vertex] = 1;
    }

    public boolean hasLoop(int vertex) {
        if (!isValidVertex(vertex)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        return adjacencyMatrix[vertex][vertex] > 0;
    }

    public int[][] copyGraphMatrix() {
        int n = adjacencyMatrix.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    public int getNumEdges() {
        int n = adjacencyMatrix.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (adjacencyMatrix[i][j] > 0) count++;
            }
        }
        return count;
    }

    public boolean contains(int value) {
        for (int[] row : adjacencyMatrix) {
            for (int cell : row) {
                if (cell == value) return true;
            }
        }
        return false;
    }

    public int[] getLine(int numVert) {
        if (!isValidVertex(numVert)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        return adjacencyMatrix[numVert];
    }

    public boolean isValidVertices(int... vertices) {
        for (int vertex : vertices) {
            if (!isValidVertex(vertex)) return false;
        }
        return true;
    }

    public List<LegacyIntGraphMatrixAdapter> removeBridges() {
        int n = adjacencyMatrix.length;
        boolean[] visited = new boolean[n];
        int[] disc = new int[n];
        int[] low = new int[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] time = {0};
        List<int[]> bridges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                bridgeDfs(i, visited, disc, low, parent, time, bridges);
            }
        }

        if (bridges.isEmpty()) {
            return List.of(this);
        }

        for (int[] e : bridges) {
            int u = e[0], v = e[1];
            if (u >= 0 && v >= 0 && u < n && v < n) {
                removeEdge(u, v);
            }
        }

        List<LegacyIntGraphMatrixAdapter> components = new ArrayList<>();
        for (Set<Integer> comp : GraphAlgorithms.connectedComponents(delegate)) {
            LegacyIntGraphAdapter legacyList = new LegacyIntGraphAdapter(delegate);
            Set<Integer>[] sub = legacyList.getSubgraph(comp);
            components.add(new LegacyIntGraphMatrixAdapter(sub));
        }
        return components;
    }

    private void bridgeDfs(
            int u,
            boolean[] visited,
            int[] disc,
            int[] low,
            int[] parent,
            int[] time,
            List<int[]> bridges
    ) {
        visited[u] = true;
        disc[u] = low[u] = ++time[0];
        for (int v = 0; v < adjacencyMatrix.length; v++) {
            if (adjacencyMatrix[u][v] <= 0) continue;
            if (!visited[v]) {
                parent[v] = u;
                bridgeDfs(v, visited, disc, low, parent, time, bridges);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u]) {
                    bridges.add(new int[]{u, v});
                }
            } else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }
}
