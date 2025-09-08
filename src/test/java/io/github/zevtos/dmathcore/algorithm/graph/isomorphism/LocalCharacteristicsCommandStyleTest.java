package io.github.zevtos.dmathcore.algorithm.graph.isomorphism;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalCharacteristicsCommandStyleTest {

    @Test
    void executesWithCommandLikeInput() {
        List<String> inputData = new ArrayList<>();
        inputData.add("0");
        inputData.add("0");
        inputData.add("0");
        inputData.add("0");
        inputData.add("0");
        inputData.add("0");
        inputData.add("3");
        inputData.add("3");
        inputData.add("2 2 6 6 8");
        inputData.add("1 1 3 8");
        inputData.add("2 3");
        inputData.add("8");
        inputData.add("5 7");
        inputData.add("1 1 7");
        inputData.add("5 6");
        inputData.add("1 2 4");
        inputData.add("0");
        inputData.add("7");
        inputData.add("7");
        inputData.add("0");
        inputData.add("0");
        inputData.add("0");
        inputData.add("0");
        inputData.add("0");
        inputData.add("2");
        inputData.add("156");
        inputData.add("4 8");
        inputData.add("3 6 6");
        inputData.add("2 6 6 7");
        inputData.add("2 4 4 5 5");
        inputData.add("5 7");
        inputData.add("3 8");

        LocalCharacteristicsInputParser parser = new LocalCharacteristicsInputParser();
        int dataSize = inputData.size();
        int quarterSize = dataSize / 4;
        int[][] adjacencyList1Arcs = parser.parseAdjacencyList(inputData.subList(0, quarterSize));
        int[][] adjacencyList1Edges = parser.parseAdjacencyList(inputData.subList(quarterSize, 2 * quarterSize));
        int[][] adjacencyList2Arcs = parser.parseAdjacencyList(inputData.subList(2 * quarterSize, 3 * quarterSize));
        int[][] adjacencyList2Edges = parser.parseAdjacencyList(inputData.subList(3 * quarterSize, dataSize));

        LocalCharacteristicsAlgorithm solver = new LocalCharacteristicsAlgorithm();
        solver.initialize(new int[][][]{adjacencyList1Arcs, adjacencyList1Edges, adjacencyList2Arcs, adjacencyList2Edges});
        String result = solver.execute();
        assertTrue(result.contains("```Алгоритм Локальных Характеристик"));
        assertTrue(result.contains("S0(G):"));
        assertTrue(result.contains("S0(H):"));
        assertTrue(result.contains("Graphs are "));
    }
}
