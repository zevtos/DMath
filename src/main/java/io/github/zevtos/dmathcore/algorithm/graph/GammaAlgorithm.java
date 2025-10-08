package io.github.zevtos.dmathcore.algorithm.graph;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyIntGraphAdapter;

import java.util.*;

public class GammaAlgorithm {

    private LegacyIntGraphAdapter graph;

    public GammaAlgorithm(int[][] adjacencyMatrix) {
        this.graph = new LegacyIntGraphAdapter(convertMatrixToSets(adjacencyMatrix));
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
        StringBuilder steps = new StringBuilder();

        if (!graph.isConnected()) {
            steps.append("Граф не связан, решение для каждой компоненты связности:\n");
            for (Set<Integer> component : graph.getConnectedComponents()) {
                LegacyIntGraphAdapter componentGraph = new LegacyIntGraphAdapter(graph.getSubgraph(component));
                GammaAlgorithm componentAlgorithm = new GammaAlgorithm(convertToMatrix(componentGraph));
                steps.append(componentAlgorithm.execute()).append("\n");
            }
        } else if (graph.getNumVertices() == 0 || graph.getNumVertices() == 1) {
            steps.append("Граф является деревом, задача тривиальна.\n");
        } else {
            steps.append("Граф не содержит мостов, плоская укладка:\n");
            steps.append(flattenGraph(graph)).append("\n");
        }

        return steps.toString();
    }

    private int[][] convertToMatrix(LegacyIntGraphAdapter graph) {
        int n = graph.getNumVertices();
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j : graph.getNeighbors(i)) {
                matrix[i][j] = 1;
            }
        }
        return matrix;
    }

    private String flattenGraph(LegacyIntGraphAdapter graph) {
        StringBuilder steps = new StringBuilder();
        List<Integer> cycle = findMaxSimpleCycle(graph);
        if (cycle.isEmpty()) {
            steps.append("Не удалось найти максимальный цикл, используем обычный поиск цикла.\n");
            cycle = findSimpleCycle(graph);
        }
        steps.append("Найден простой цикл: ").append(cycle).append("\n");

        Map<String, List<Integer>> segments = segmentEdges(graph, cycle);
        steps.append("Сегменты после сегментации: ").append(segments).append("\n");

        // Создаем новый граф и добавляем в него ребра исходного цикла
        LegacyIntGraphAdapter newGraph = new LegacyIntGraphAdapter(graph.getNumVertices());
        for (int i = 0; i < cycle.size() - 2; i++) {
            newGraph.addEdge(cycle.get(i), cycle.get(i + 1));
        }

        while (!segments.isEmpty()) {
            Map<String, Integer> segmentEdgesCount = determineSegmentEdgesCount(newGraph, segments);
            steps.append("Количество граней для каждого сегмента: ").append(segmentEdgesCount).append("\n");

            String minSegmentKey = Collections.min(segmentEdgesCount.entrySet(), Map.Entry.comparingByValue()).getKey();
            List<Integer> segment = segments.remove(minSegmentKey);
            newGraph.addEdge(segment.get(0), segment.get(1));
            steps.append("Обработка сегмента: ").append(segment).append("\n");
            steps.append("Соединяем вершины ").append(segment.get(0)).append(" и ").append(segment.get(1)).append("\n");
        }

        steps.append("Все сегменты успешно соединены, граф связан.");
        return steps.toString();
    }

    private List<Integer> findMaxSimpleCycle(LegacyIntGraphAdapter graph) {
        List<Integer> maxCycle = new ArrayList<>();
        int n = graph.getNumVertices();

        for (int i = 0; i < n; i++) {
            boolean[] visited = new boolean[n];
            List<Integer> path = new ArrayList<>();
            findCycleUtil(graph, i, visited, path, maxCycle);
        }

        return maxCycle;
    }

    private void findCycleUtil(LegacyIntGraphAdapter graph, int v, boolean[] visited, List<Integer> path, List<Integer> maxCycle) {
        visited[v] = true;
        path.add(v);

        for (int neighbor : graph.getNeighbors(v)) {
            if (!visited[neighbor]) {
                findCycleUtil(graph, neighbor, visited, path, maxCycle);
            } else if (path.size() > 2 && neighbor == path.get(0)) {
                path.add(neighbor);
                if (path.size() > maxCycle.size()) {
                    maxCycle.clear();
                    maxCycle.addAll(new ArrayList<>(path));
                }
                path.remove(path.size() - 1);
            }
        }

        path.remove(path.size() - 1);
        visited[v] = false;
    }

    private List<Integer> findSimpleCycle(LegacyIntGraphAdapter graph) {
        List<Integer> simpleCycle = new ArrayList<>();
        boolean[] visited = new boolean[graph.getNumVertices()];
        int[] parent = new int[graph.getNumVertices()];
        Arrays.fill(parent, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);

        while (!stack.isEmpty()) {
            int node = stack.pop();
            visited[node] = true;

            for (int neighbor : graph.getNeighbors(node)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                    parent[neighbor] = node;
                } else if (neighbor != parent[node] && parent[node] != -1) {
                    List<Integer> cycle = new ArrayList<>();
                    cycle.add(neighbor);
                    for (int v = node; v != neighbor; v = parent[v]) {
                        cycle.add(v);
                    }
                    cycle.add(neighbor);
                    return cycle;
                }
            }
        }

        return simpleCycle;
    }

    private Map<String, List<Integer>> segmentEdges(LegacyIntGraphAdapter graph, List<Integer> cycle) {
        Map<String, List<Integer>> segments = new HashMap<>();
        Set<Integer> cycleSet = new HashSet<>(cycle);

        for (int i = 0; i < graph.getNumVertices(); i++) {
            for (int j : graph.getNeighbors(i)) {
                if (!(cycleSet.contains(i) && cycleSet.contains(j)) || !cycleContainsEdge(cycle, i, j)) {
                    String segmentKey = i + "-" + j;
                    if (!segments.containsKey(segmentKey)) {
                        segments.put(segmentKey, new ArrayList<>());
                    }
                    segments.get(segmentKey).add(i);
                    segments.get(segmentKey).add(j);
                    System.out.println("Добавлен сегмент: " + segmentKey + " -> " + segments.get(segmentKey));
                }
            }
        }

        return segments;
    }

    private boolean cycleContainsEdge(List<Integer> cycle, int i, int j) {
        for (int k = 0; k < cycle.size() - 1; k++) {
            int u = cycle.get(k);
            int v = cycle.get(k + 1);
            if ((u == i && v == j) || (u == j && v == i)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Integer> determineSegmentEdgesCount(LegacyIntGraphAdapter newGraph, Map<String, List<Integer>> segments) {
        Map<String, Integer> segmentEdgesCount = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : segments.entrySet()) {
            String segmentKey = entry.getKey();
            List<Integer> segment = entry.getValue();
            int start = segment.get(0);
            int end = segment.get(1);

            int count = 0;
            for (int i = 0; i < newGraph.getNumVertices(); i++) {
                for (int j = i + 1; j < newGraph.getNumVertices(); j++) {
                    if (newGraph.hasEdge(i, j)) continue;
                    if (segmentsIntersect(start, end, i, j, newGraph)) continue;
                    count++;
                }
            }

            segmentEdgesCount.put(segmentKey, count);
            System.out.println("Граней для сегмента " + segmentKey + ": " + count);
        }

        return segmentEdgesCount;
    }

    private boolean segmentsIntersect(int x1, int y1, int x2, int y2, LegacyIntGraphAdapter graph) {
        // Простая проверка на пересечение отрезков (x1, y1) и (x2, y2)
        // Пример алгоритма - проверка пересечения двух отрезков на плоскости
        return (Math.min(x1, y1) < Math.max(x2, y2)) && (Math.max(x1, y1) > Math.min(x2, y2)) &&
                (Math.min(x2, y2) < Math.max(x1, y1)) && (Math.max(x2, y2) > Math.min(x1, y1));
    }
}
