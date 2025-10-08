package io.github.zevtos.dmathcore.algorithm.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RobertsFloresAlgorithmTest {

    @Test
    void testRobertsFloresAlgorithm() {
        // Test graph from the original main method
        int[][] adjacencyMatrix = {
                {0, 1, 0, 1, 0, 0}, // a -> b, a -> d
                {0, 0, 0, 0, 0, 1}, // b -> f
                {0, 0, 0, 0, 1, 1}, // c -> e, c -> f
                {0, 0, 1, 0, 1, 0}, // d -> c, d -> e
                {0, 1, 0, 0, 0, 0}, // e -> b
                {1, 0, 0, 0, 1, 0}  // f -> a, f -> e
        };

        RobertsFloresAlgorithm algorithm = new RobertsFloresAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("RobertsFloresAlgorithm"));
        assertTrue(result.contains("Добавлена вершина"));
        assertTrue(result.contains("Удалена вершина"));
    }

    @Test
    void testEmptyGraph() {
        int[][] adjacencyMatrix = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        RobertsFloresAlgorithm algorithm = new RobertsFloresAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("RobertsFloresAlgorithm"));
    }

    @Test
    void testSingleVertex() {
        int[][] adjacencyMatrix = {{0}};

        RobertsFloresAlgorithm algorithm = new RobertsFloresAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("RobertsFloresAlgorithm"));
    }
}
