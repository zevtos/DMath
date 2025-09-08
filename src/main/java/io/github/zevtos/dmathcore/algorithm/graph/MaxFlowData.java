package io.github.zevtos.dmathcore.algorithm.graph;

public class MaxFlowData {
    private final int[][] capacity;
    private final int source;
    private final int sink;

    public MaxFlowData(int[][] capacity, int source, int sink) {
        this.capacity = capacity;
        this.source = source;
        this.sink = sink;
    }

    public int[][] getCapacity() {
        return capacity;
    }

    public int getSource() {
        return source;
    }

    public int getSink() {
        return sink;
    }
}
