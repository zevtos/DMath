package io.github.zevtos.dmathcore.domain.graph.legacy;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Legacy-compatible colored graph adapter over adjacency matrix representation.
 */
public class LegacyColoredGraphMatrixAdapter {
    private final LegacyIntGraphMatrixAdapter base;
    private int[] vertexColors; // 0 = uncolored

    public LegacyColoredGraphMatrixAdapter(int numVertices) {
        this.base = new LegacyIntGraphMatrixAdapter(numVertices);
        initializeVertexColors();
    }

    public LegacyColoredGraphMatrixAdapter(java.util.Set<Integer>[] graph) {
        this.base = new LegacyIntGraphMatrixAdapter(graph);
        initializeVertexColors();
    }

    public LegacyColoredGraphMatrixAdapter(int[][] adjacencyMatrix) {
        this.base = new LegacyIntGraphMatrixAdapter(adjacencyMatrix);
        initializeVertexColors();
    }

    private void initializeVertexColors() {
        vertexColors = new int[base.getNumVertices()];
    }

    public int getNumVertices() { return base.getNumVertices(); }

    public boolean isValidVertex(int vertex) { return base.isValidVertex(vertex); }

    public void setColor(int vertex, int color) {
        if (!isValidVertex(vertex)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        vertexColors[vertex] = color;
    }

    public int getColor(int vertex) {
        if (!isValidVertex(vertex)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        return vertexColors[vertex];
    }

    public boolean isColored(int vertex) {
        if (!isValidVertex(vertex)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        return vertexColors[vertex] != 0;
    }

    public boolean containsColor(int color) {
        for (int c : vertexColors) {
            if (c == color) return true;
        }
        return false;
    }

    public int getMaxDegreeVertex() {
        int maxDegreeVertex = -1;
        int maxDegree = Integer.MIN_VALUE;
        int[][] adj = base.getAdjacencyMatrix();
        for (int i = 0; i < getNumVertices(); i++) {
            if (!isColored(i)) {
                int degree = 0;
                for (int j = 0; j < getNumVertices(); j++) {
                    if (adj[i][j] == 1) degree++;
                }
                if (degree > maxDegree) {
                    maxDegree = degree;
                    maxDegreeVertex = i;
                }
            }
        }
        return maxDegreeVertex;
    }

    public int getMaxDegreeVertex(int[] degrees) {
        int maxDegreeVertex = -1;
        int maxDegree = Integer.MIN_VALUE;
        for (int i = 0; i < degrees.length; i++) {
            if (vertexColors[i] == 0 && degrees[i] > maxDegree) {
                maxDegree = degrees[i];
                maxDegreeVertex = i;
            }
        }
        return maxDegreeVertex;
    }

    public static int getMaxDegreeVertex(int[] degrees, boolean[] acces) {
        int maxDegreeVertex = -1;
        int maxDegree = Integer.MIN_VALUE;
        for (int i = 0; i < degrees.length; i++) {
            if (!acces[i] && degrees[i] > maxDegree) {
                maxDegree = degrees[i];
                maxDegreeVertex = i;
            }
        }
        return maxDegreeVertex;
    }

    public static int getMaxDegreeVertex(int[] degrees, ArrayList<Integer> acces) {
        int maxDegreeVertex = -1;
        int maxDegree = Integer.MIN_VALUE;
        for (int i = 0; i < degrees.length; i++) {
            if (acces.get(i) != 0) continue;
            if (degrees[i] > maxDegree) {
                maxDegree = degrees[i];
                maxDegreeVertex = i;
            }
        }
        return maxDegreeVertex;
    }

    private void colorEmptySubgraph(int vertex, int color) {
        setColor(vertex, color);
        removeRowAndColumn(vertex);
    }

    public void colorGraph() {
        int colorCounter = 1;
        while (!allVerticesColored()) {
            int maxDegreeVertex = getMaxDegreeVertex();
            if (maxDegreeVertex == -1) break;
            colorEmptySubgraph(maxDegreeVertex, colorCounter);
            colorCounter++;
        }
        System.out.println("Количество необходимых цветов: " + (colorCounter - 1));
    }

    private boolean allVerticesColored() {
        for (int color : vertexColors) {
            if (color == 0) return false;
        }
        return true;
    }

    public void removeVertex(int vertex) {
        base.removeVertex(vertex);
        vertexColors = Arrays.copyOf(vertexColors, base.getNumVertices());
    }

    public int[] getDegrees() {
        int n = getNumVertices();
        int[][] adj = base.getAdjacencyMatrix();
        int[] degrees = new int[n];
        for (int i = 0; i < n; i++) {
            degrees[i] = -1;
            for (int j = 0; j < n; j++) {
                if (adj[i][j] == 1) {
                    if (degrees[i] == -1) degrees[i]++;
                    degrees[i]++;
                } else if (adj[i][j] == 0) {
                    if (degrees[i] == -1) degrees[i] = 0;
                }
            }
        }
        return degrees;
    }

    public void removeRowAndColumn(int index) {
        if (!isValidVertex(index)) {
            throw new IllegalArgumentException("Некорректная вершина");
        }
        int[][] adj = base.getAdjacencyMatrix();
        int n = getNumVertices();
        for (int i = 0; i < n; i++) {
            adj[index][i] = -1;
            adj[i][index] = -1;
        }
    }

    public void set(int i, int j, int value) {
        int[][] adj = base.getAdjacencyMatrix();
        adj[i][j] = value;
    }

    public int[][] getAdjacencyMatrix() { return base.getAdjacencyMatrix(); }

    public String adjacencyMatrixToString() {
        StringBuilder text_return = new StringBuilder("Матрица смежности:");
        int[][] adjacencyMatrix = base.getAdjacencyMatrix();
        int numVertices = getNumVertices();
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                text_return.append(String.format("%4s", adjacencyMatrix[i][j] + " "));
            }
            text_return.append("\n");
        }
        return text_return.toString();
    }

    public java.util.Set<Integer> getNeighbors(int vertex) {
        java.util.Set<Integer> neighbors = new java.util.HashSet<>();
        int[][] adj = base.getAdjacencyMatrix();
        for (int i = 0; i < getNumVertices(); i++) {
            if (adj[vertex][i] == 1) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    public java.util.List<Integer> getLine(int vertex) {
        int[][] adj = base.getAdjacencyMatrix();
        java.util.List<Integer> line = new java.util.ArrayList<>();
        for (int i = 0; i < getNumVertices(); i++) {
            line.add(adj[vertex][i]);
        }
        return line;
    }

    public static int getMaxDegreeVertex(int[] degrees, java.util.List<Integer> accessible) {
        int maxDegreeVertex = -1;
        int maxDegree = Integer.MIN_VALUE;
        for (int i = 0; i < degrees.length; i++) {
            if (i < accessible.size() && accessible.get(i) == 0 && degrees[i] > maxDegree) {
                maxDegree = degrees[i];
                maxDegreeVertex = i;
            }
        }
        return maxDegreeVertex;
    }
}
