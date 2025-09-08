package io.github.zevtos.dmathcore.domain.graph.legacy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Legacy-compatible weighted matrix graph for int vertices [0..N-1].
 *
 * <p>Wraps {@link LegacyIntGraphMatrixAdapter} to manage unweighted connectivity and
 * maintains an additional weights matrix. Integer.MAX_VALUE denotes no path.</p>
 */
public class LegacyIntWeightedGraphMatrixAdapter {
    private final LegacyIntGraphMatrixAdapter base;
    protected int[][] weightsMatrix;

    public LegacyIntWeightedGraphMatrixAdapter(int numVertices) {
        this.base = new LegacyIntGraphMatrixAdapter(numVertices);
        initializeWeightsMatrix();
    }

    public LegacyIntWeightedGraphMatrixAdapter(java.util.Set<Integer>[] graph) {
        this.base = new LegacyIntGraphMatrixAdapter(graph);
        initializeWeightsMatrix();
    }

    public LegacyIntWeightedGraphMatrixAdapter(int[][] adjacencyMatrix, int[][] weightsMatrix) {
        this.base = new LegacyIntGraphMatrixAdapter(adjacencyMatrix);
        int n = adjacencyMatrix.length;
        this.weightsMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(weightsMatrix[i], 0, this.weightsMatrix[i], 0, n);
        }
    }

    private void initializeWeightsMatrix() {
        int n = base.getNumVertices();
        weightsMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(weightsMatrix[i], Integer.MAX_VALUE);
        }
    }

    public int getNumVertices() {
        return base.getNumVertices();
    }

    public boolean isValidVertices(int... vertices) {
        return base.isValidVertices(vertices);
    }

    public void addEdge(int a, int b) {
        base.addEdge(a, b);
        // weight is set separately via setWeight
    }

    public void removeEdge(int a, int b) {
        base.removeEdge(a, b);
        if (a >= 0 && b >= 0 && a < getNumVertices() && b < getNumVertices()) {
            weightsMatrix[a][b] = Integer.MAX_VALUE;
            weightsMatrix[b][a] = Integer.MAX_VALUE;
        }
    }

    public boolean hasEdge(int a, int b) {
        return base.hasEdge(a, b);
    }

    public void addLoop(int vertex) {
        base.addLoop(vertex);
    }

    public boolean hasLoop(int vertex) {
        return base.hasLoop(vertex);
    }

    public boolean hasPath(int source, int destination) {
        return base.hasPath(source, destination);
    }

    public void removeVertex(int vertex) {
        base.removeVertex(vertex);
        // Update weights by removing row and column
        int n = getNumVertices();
        int[][] newWeights = new int[n][n];
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

    public void setWeight(int a, int b, int weight) {
        if (!isValidVertices(a, b)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        weightsMatrix[a][b] = weight;
    }

    public void setNeorWeight(int i, int j, int weight) {
        if (!isValidVertices(i, j)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        weightsMatrix[i][j] = weight;
        weightsMatrix[j][i] = weight;
    }

    public int getWeight(int a, int b) {
        if (!isValidVertices(a, b)) {
            throw new IllegalArgumentException("Некорректные вершины");
        }
        return weightsMatrix[a][b];
    }

    public void displayAdjacencyMatrix() {
        System.out.println("Матрица смежности с весами:");
        int n = getNumVertices();
        int[][] adj = base.getAdjacencyMatrix();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (weightsMatrix[i][j] == Integer.MAX_VALUE) {
                    System.out.printf("%5s", adj[i][j] + "(inf) ");
                } else {
                    System.out.printf("%5s", adj[i][j] + "(" + weightsMatrix[i][j] + ") ");
                }
            }
            System.out.println();
        }
    }

    public int[][] getWeightsMatrix() {
        return weightsMatrix;
    }

    public int[][] copyWeightMatrix() {
        int n = getNumVertices();
        int[][] ret = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(weightsMatrix[i], 0, ret[i], 0, n);
        }
        return ret;
    }

    public void subtractWeight(int rowIndex, int columnIndex, int value) {
        weightsMatrix[rowIndex][columnIndex] -= value;
    }

    public void subtraction(int indRow, int indColumn, int size) {
        weightsMatrix[indRow][indColumn] -= size;
    }

    public Map<Integer, Integer> getZeros() {
        Map<Integer, Integer> zeros = new HashMap<>();
        int n = getNumVertices();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (weightsMatrix[i][j] == 0) {
                    zeros.put(i, j);
                    break;
                }
            }
        }
        return zeros;
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
                    if (weightsMatrix[i][j] == Integer.MAX_VALUE) {
                        System.out.printf("%5s", adj[i][j] + "(inf) ");
                    } else {
                        System.out.printf("%5s", adj[i][j] + "(" + weightsMatrix[i][j] + ") ");
                    }
                }
            }
            System.out.println();
        }
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
                if (weightsMatrix[i][j] == Integer.MAX_VALUE) {
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
