package io.github.zevtos.dmathcore.algorithm.graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MaxFlowAlgorithm {
    private int[][] capacity;
    private int source;
    private int sink;
    private int numVertices;
    private int[][] residualCapacity;

    public void initialize(MaxFlowData data) {
        this.capacity = data.getCapacity();
        this.source = data.getSource();
        this.sink = data.getSink();
        this.numVertices = capacity.length;
        this.residualCapacity = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(capacity[i], 0, residualCapacity[i], 0, numVertices);
        }
    }

    public String execute() {
        int maxFlow = fordFulkerson();
        return "Максимальный поток: " + maxFlow;
    }

    private int fordFulkerson() {
        int maxFlow = 0;
        int[] parent = new int[numVertices];

        while (bfs(parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualCapacity[u][v]);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residualCapacity[u][v] -= pathFlow;
                residualCapacity[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    private boolean bfs(int[] parent) {
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && residualCapacity[u][v] > 0) {
                    if (v == sink) {
                        parent[v] = u;
                        return true;
                    }
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return false;
    }
}
