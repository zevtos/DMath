package io.github.zevtos.dmathcore.algorithm.graph;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyDoubleWeightedGraphMatrixAdapter;

import java.util.*;

public class PrimAlgorithm {
    private LegacyDoubleWeightedGraphMatrixAdapter graph;
    private int numVertices;
    private boolean[] inMST;
    private double[] key;
    private int[] parent;
    private List<String> result;  // Для записи шагов решения
    private List<Edge> edges;  // Для записи рёбер MST

    private static class Edge {
        int src, dest;
        double weight;

        Edge(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + (char) ('a' + src) + ", " + (char) ('a' + dest) + ")";
        }
    }

    public PrimAlgorithm(double[][] weightsMatrix) {
        this.graph = new LegacyDoubleWeightedGraphMatrixAdapter(createAdjacencyMatrix(weightsMatrix), weightsMatrix);
        this.numVertices = graph.getNumVertices();
        this.inMST = new boolean[numVertices];
        this.key = new double[numVertices];
        this.parent = new int[numVertices];
        this.result = new ArrayList<>();
        this.edges = new ArrayList<>();
        Arrays.fill(key, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        key[0] = 0; // Начальная вершина

        result.add("Инициализация завершена. Начинаем с вершины a.");
    }

    private int[][] createAdjacencyMatrix(double[][] weightsMatrix) {
        int n = weightsMatrix.length;
        int[][] adjacencyMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacencyMatrix[i][j] = (weightsMatrix[i][j] != Double.POSITIVE_INFINITY) ? 1 : 0;
            }
        }
        return adjacencyMatrix;
    }

    public String execute() {
        for (int count = 0; count < numVertices - 1; count++) {
            int u = minKey();
            if (u == -1) {
                result.add("Нет доступных вершин для добавления в MST.");
                break;
            }
            inMST[u] = true;
            result.add("Вершина " + (char) ('a' + u) + " добавлена в MST.");

            for (int v = 0; v < numVertices; v++) {
                if (!inMST[v] && graph.hasEdge(u, v)) {
                    double weight = graph.getWeight(u, v);
                    if (weight < key[v]) {
                        key[v] = weight;
                        parent[v] = u;
                        result.add("Обновление: " + (char) ('a' + v) + " с ключом " + weight + " и родителем " + (char) ('a' + u));
                    }
                }
            }
        }

        for (int i = 1; i < numVertices; i++) {
            if (parent[i] != -1) {
                edges.add(new Edge(parent[i], i, graph.getWeight(i, parent[i])));
            }
        }

        return buildResult();
    }

    private int minKey() {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;

        for (int v = 0; v < numVertices; v++) {
            if (!inMST[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }

        return minIndex;
    }

    private String buildResult() {
        StringBuilder sb = new StringBuilder("```PrimAlgorithm\n");
        sb.append("Мини-гайд по алгоритму Прима:\n");
        sb.append("MST - минимальное остовное дерево.\n");
        sb.append("1. При добавлении вершины в MST происходит добавление соответствующего ребра в результат.\n");
        sb.append("2. Ребро выбирается по принципу минимального доступного веса.\n");
        sb.append("3. Столбец, соответствующий добавленной вершине, исключается из рассмотрения.\n");
        sb.append("4. Строка, соответствующая новой вершине, добавляется в рассмотрение.\n");
        sb.append("\n№\tДействие\n");
        for (int i = 0; i < result.size(); i++) {
            sb.append(i + 1).append("\t").append(result.get(i)).append("\n");
        }
        sb.append("\nРезультат:\n");
        sb.append("№\tРебро\tВес\n");
        double totalWeight = 0;
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            sb.append(i + 1).append("\t").append(edge).append("\t").append(edge.weight).append("\n");
            totalWeight += edge.weight;
        }
        sb.append("Суммарный вес рёбер MOD: ").append(totalWeight).append("\n");
        sb.append("```");
        return sb.toString();
    }
}
