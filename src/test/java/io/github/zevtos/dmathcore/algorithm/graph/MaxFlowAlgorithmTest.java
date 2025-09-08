package io.github.zevtos.dmathcore.algorithm.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxFlowAlgorithmTest {

    @Test
    void testMaxFlow() {
        int[][] inputMatrix = {
                {0, 3, 6, 0, 13, 6},
                {0, 6, 10, 11, 11},
                {0, 0, 12, 6, 0},
                {0, 0, 0, 2, 0},
                {0, 0, 0, 0, 0}
        };

        MaxFlowData data = new MaxFlowData(inputMatrix, 0, 5);
        MaxFlowAlgorithm algorithm = new MaxFlowAlgorithm();
        algorithm.initialize(data);
        String result = algorithm.execute();

        assertTrue(result.contains("Максимальный поток:"));
    }
}
    