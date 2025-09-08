package io.github.zevtos.dmathcore.domain.graph.legacy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;

/**
 * Legacy-compatible double-weighted matrix graph for int vertices [0..N-1].
 */
public class LegacyDoubleWeightedGraphMatrixAdapter {
    private final LegacyIntGraphMatrixAdapter base;
    protected double[][] weightsMatrix;

    public LegacyDoubleWeightedGraphMatrixAdapter(int numVertices) {
        this.base = new LegacyIntGraphMatrixAdapter(numVertices);
        initializeWeightsMatrix();
    }

    public LegacyDoubleWeightedGraphMatrixAdapter(Set<Integer>[] graph) {
        this.base = new LegacyIntGraphMatrixAdapter(graph);
        initializeWeightsMatrix();
    }

    public LegacyDoubleWeightedGraphMatrixAdapter(int[][] adjacencyMatrix, double[][] weightsMatrix) {
        this.base = new LegacyIntGraphMatrixAdapter(adjacencyMatrix);
        int n = adjacencyMatrix.length;
        this.weightsMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(weightsMatrix[i], 0, this.weightsMatrix[i], 0, n);
        }
    }

    private void initializeWeightsMatrix() {
        int n = base.getNumVertices();
        weightsMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(weightsMatrix[i], Double.POSITIVE_INFINITY);
        }
    }

    public int getNumVertices() { return base.getNumVertices(); }

    public boolean isValidVertices(int... vertices) { return base.isValidVertices(vertices); }

    public void addEdge(int a, int b) { base.addEdge(a, b); }

    public void removeEdge(int a, int b) {
        base.removeEdge(a, b);
        if (a >= 0 && b >= 0 && a < getNumVertices() && b < getNumVertices()) {
            weightsMatrix[a][b] = Double.POSITIVE_INFINITY;
            weightsMatrix[b][a] = Double.POSITIVE_INFINITY;
        }
    }

    public boolean hasEdge(int a, int b) { return base.hasEdge(a, b); }

    public void addLoop(int vertex) { base.addLoop(vertex); }

    public boolean hasLoop(int vertex) { return base.hasLoop(vertex); }

    public boolean hasPath(int source, int destination) { return base.hasPath(source, destination); }

    public void removeVertex(int vertex) {
        base.removeVertex(vertex);
        int n = getNumVertices();
        double[][] newWeights = new double[n][n];
        int oldN = n + 1;
        int newRow = 0;
        for (int i = 0; i < oldN; i++) {
            if (i == vertex) continue;
            int newCol = 0;
            for (int j = 0; j < oldN; j++) {
                if (j == vertex) continue;
                newWeights[newRow][newCol] = weightsMatrix[i][j];
                newCol++;
            }
            newRow++;
        }
        weightsMatrix = newWeights;
    }

    public void setWeight(int a, int b, double weight) {
        if (!isValidVertices(a, b)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        weightsMatrix[a][b] = weight;
        weightsMatrix[b][a] = weight;
    }

    public void setNeorWeight(int i, int j, double weight) {
        if (!isValidVertices(i, j)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        weightsMatrix[i][j] = weight;
    }

    public double getWeight(int a, int b) {
        if (!isValidVertices(a, b)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        return weightsMatrix[a][b];
    }

    public void customDisplayAdjacencyMatrix(Map<Integer, Integer> zeros) {
        System.out.println("Матрица смежности с весами:");
        int n = getNumVertices();
        System.out.printf("%3s", "");
        for (int i = 0; i < n; i++) {
            System.out.printf("%5s", (i + 1) + "   ");
        }
        System.out.println();
        int[][] adj = base.getAdjacencyMatrix();
        for (int i = 0; i < n; i++) {
            System.out.printf("%3s", (i + 1) + "  ");
            for (int j = 0; j < n; j++) {
                if (zeros.containsKey(i) && zeros.get(i) == j) {
                    System.out.printf("%5s", adj[i][j] + "(" + weightsMatrix[i][j] + "*) ");
                } else {
                    if (Double.isInfinite(weightsMatrix[i][j])) {
                        System.out.printf("%5s", adj[i][j] + "(inf) ");
                    } else {
                        System.out.printf("%5s", adj[i][j] + "(" + weightsMatrix[i][j] + ") ");
                    }
                }
            }
            System.out.println();
        }
    }

    public Map<Integer, Integer> getZeros() {
        Map<Integer, Integer> zeros = new HashMap<>();
        int n = getNumVertices();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (weightsMatrix[i][j] == 0.0) {
                    zeros.put(i, j);
                    break;
                }
            }
        }
        return zeros;
    }

    public double[][] getWeightsMatrix() { return weightsMatrix; }

    public double[][] copyWeightMatrix() {
        int n = getNumVertices();
        double[][] ret = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(weightsMatrix[i], 0, ret[i], 0, n);
        }
        return ret;
    }

    public void subtractWeight(int rowIndex, int columnIndex, double value) {
        weightsMatrix[rowIndex][columnIndex] -= value;
    }

    public void subtraction(int indRow, int indColumn, double size) {
        weightsMatrix[indRow][indColumn] -= size;
    }

    public String displayMatrixs(Map<Integer, Integer> zeros, String sig) {
        StringBuilder result = new StringBuilder();
        int n = getNumVertices();
        int[][] adj = base.getAdjacencyMatrix();

        result.append("Матрица смежности:\n");
        result.append(String.format("%3s", ""));
        for (int i = 0; i < n; ) {
            if (zeros.containsValue(i++)) {
                result.append(String.format("%5s", i + sig + "   "));
                continue;
            }
            result.append(String.format("%5s", i + "   "));
        }
        result.append("\n");

        for (int i = 0; i < n; i++) {
            result.append(String.format("%3s", (i + 1) + "  "));
            for (int j = 0; j < n; j++) {
                if (zeros.containsKey(i) && zeros.get(i) == j) {
                    result.append(String.format("%5s", adj[i][j] + "(" + weightsMatrix[i][j] + "*)"));
                    continue;
                }
                result.append(String.format("%5s", adj[i][j] + "(" + weightsMatrix[i][j] + ")" + " "));
            }
            result.append("\n");
        }

        return result.toString();
    }

    public String adjacencyMatrixToString() {
        StringBuilder result = new StringBuilder();
        int n = getNumVertices();
        int[][] adj = base.getAdjacencyMatrix();

        result.append("Матрица смежности с весами:\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Double.isInfinite(weightsMatrix[i][j])) {
                    result.append(String.format("%5s", adj[i][j] + "(inf) "));
                } else {
                    result.append(String.format("%5s", adj[i][j] + "(" + weightsMatrix[i][j] + ") "));
                }
            }
            result.append("\n");
        }

        return result.toString();
    }
}
