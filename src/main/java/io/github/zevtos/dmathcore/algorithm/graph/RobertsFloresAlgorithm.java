package io.github.zevtos.dmathcore.algorithm.graph;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyIntGraphAdapter;

import java.util.*;

public class RobertsFloresAlgorithm {
    private LegacyIntGraphAdapter graph;
    private List<String> result;  // Для записи шагов решения

    public RobertsFloresAlgorithm(int[][] adjacencyMatrix) {
        this.graph = new LegacyIntGraphAdapter(convertMatrixToSets(adjacencyMatrix));
        this.result = new ArrayList<>();
    }

    private Set<Integer>[] convertMatrixToSets(int[][] matrix) {
        int n = matrix.length;
        @SuppressWarnings("unchecked")
        Set<Integer>[] sets = new HashSet[n];
        for (int i = 0; i < n; i++) {
            sets[i] = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] > 0) {
                    sets[i].add(j);
                }
            }
        }
        return sets;
    }

    public String execute() {
        List<Integer> path = new ArrayList<>();
        path.add(0);
        result.add("Добавлена вершина  " + (char) ('a') + "     " + pathToString(path));
        findHamiltonianCycles(path, 0);
        return buildResult();
    }

    private void findHamiltonianCycles(List<Integer> path, int currentVertex) {
        if (path.size() == graph.getNumVertices()) {
            if (graph.getNeighbors(currentVertex).contains(path.get(0))) {
                result.add("Добавлена вершина  " + (char)('a' + currentVertex) + "     " + pathToString(path) + "\t  +");
            } else {
                result.add("Добавлена вершина  " + (char)('a' + currentVertex) + "     " + pathToString(path));
            }
            return;
        }

        Set<Integer> neighbors = new HashSet<>(graph.getNeighbors(currentVertex));
        for (int neighbor : neighbors) {
            if (!path.contains(neighbor)) {
                path.add(neighbor);
                result.add("Добавлена вершина  " + (char) ('a' + neighbor) + "     " + pathToString(path));
                findHamiltonianCycles(path, neighbor);
                path.remove(path.size() - 1);
                result.add("Удалена вершина    " + (char) ('a' + neighbor) + "     " + pathToString(path));
            }
        }
    }

    private String pathToString(List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        for (int vertex : path) {
            sb.append((char) ('a' + vertex)).append(" ");
        }
        return sb.toString().trim();
    }

    private String buildResult() {
        StringBuilder sb = new StringBuilder("```RobertsFloresAlgorithm\n");
        sb.append("С помощью + обозначены циклы\n");
        sb.append(String.format("%3s\t\t%-15s", "№", "Действие"));
        for (int i = 0; i < result.size(); i++) {
            sb.append(String.format("%3d", i + 1)).append("\t").append(result.get(i)).append("\n");
        }
        sb.append("```");
        return sb.toString();
    }
}
