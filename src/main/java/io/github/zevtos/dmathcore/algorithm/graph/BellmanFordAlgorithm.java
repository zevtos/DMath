package io.github.zevtos.dmathcore.algorithm.graph;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyDoubleWeightedGraphMatrixAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BellmanFordAlgorithm {
    private LegacyDoubleWeightedGraphMatrixAdapter graph;
    private int source;
    private int target;
    private double[] distances;
    private int[] predecessors;
    private List<double[]> lambda = new ArrayList<>();

    public BellmanFordAlgorithm(LegacyDoubleWeightedGraphMatrixAdapter graph, int source, int target) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        int numVertices = graph.getNumVertices();
        this.distances = new double[numVertices];
        this.predecessors = new int[numVertices];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(predecessors, -1);
        distances[source] = 0;
        
        // Инициализация первой лямбда-матрицы
        lambda.add(distances.clone());
    }

    public String execute() {
        int V = graph.getNumVertices();

        boolean hasChanged;
        int iteration = 0;

        do {
            hasChanged = false;
            iteration++;
            double[] newDistances = distances.clone();

            for (int u = 0; u < V; u++) {
                for (int v = 0; v < V; v++) {
                    double weight = graph.getWeight(u, v);
                    if (weight != Double.POSITIVE_INFINITY && distances[u] != Double.POSITIVE_INFINITY && distances[u] + weight < newDistances[v]) {
                        newDistances[v] = distances[u] + weight;
                        predecessors[v] = u;
                        hasChanged = true;
                    }
                }
            }

            if (hasChanged) {
                distances = newDistances;
                lambda.add(distances.clone());
            }

        } while (hasChanged && iteration < V - 1);

        // Проверка на наличие отрицательных циклов
        for (int u = 0; u < V; u++) {
            for (int v = 0; v < V; v++) {
                double weight = graph.getWeight(u, v);
                if (weight != Double.POSITIVE_INFINITY && distances[u] != Double.POSITIVE_INFINITY && distances[u] + weight < distances[v]) {
                    return "Graph contains a negative weight cycle";
                }
            }
        }

        return buildResult();
    }

    private String buildResult() {
        StringBuilder result = new StringBuilder("```BellmanFord\n");
        result.append("Матрица W\n ");
        for (int i = 0; i < graph.getNumVertices(); i++) {
            result.append(String.format("%7s", (char) ('a' + i)));
        }
        result.append("\n");
        for (int i = 0; i < graph.getNumVertices(); i++) {
            result.append((char) ('a' + i)).append("\t");
            for (int j = 0; j < graph.getNumVertices(); j++) {
                if (graph.getWeight(i, j) == Double.POSITIVE_INFINITY) {
                    result.append(String.format("%6s", "-")).append("\t");
                } else {
                    result.append(String.format("%6.1f", graph.getWeight(i, j))).append("\t");
                }
            }
            result.append("\n");
        }

        result.append("\nТаблица λ\n ");
        for (int i = 0; i <= lambda.size(); i++) {
            result.append(String.format("%7s", "λ^" + i));
        }
        result.append("\n");
        for (int i = 0; i < distances.length; i++) {
            result.append((char) ('a' + i)).append("\t");
            for (double[] lambdaStep : lambda) {
                result.append(lambdaStep[i] == Double.POSITIVE_INFINITY ? String.format("%6s", "-") : String.format("%6.1f", lambdaStep[i])).append("\t");
            }
            result.append(lambda.getLast()[i] == Double.POSITIVE_INFINITY ? String.format("%6s", "-") : String.format("%6.1f", lambda.getLast()[i])).append("\t");
            result.append("\n");
        }

        double pathWeight = 0;
        StringBuilder path = new StringBuilder();
        int current = target;
        path.insert(0, (char) ('a' + current));
        while (predecessors[current] != -1) {
            path.insert(0, (char) ('a' + predecessors[current]) + " -> ");
            pathWeight += graph.getWeight(predecessors[current], current);
            current = predecessors[current];
        }
        result.append("\nДлина маршрута: ").append(findPathLength());
        result.append("\nВес маршрута: ").append(pathWeight);
        result.append("\nПуть: ").append(path);
        result.append("\nВыбирать вершины в путь необходимо с предпоследнего столбца таблицы λ в обратном направлении(начиная с предпоследнего, заканчивая первым)");
        result.append("\n```");
        return result.toString();
    }

    private int findPathLength() {
        int length = 0;
        int current = target;
        while (predecessors[current] != -1) {
            current = predecessors[current];
            length++;
        }
        return length;
    }
}
