package io.github.zevtos.dmathcore.algorithm.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeuristicGraphColoringAlgorithmTest {

    @Test
    void testHeuristicGraphColoringAlgorithm() {
        // Test graph with known coloring
        int[][] adjacencyMatrix = {
                {0, 1, 1, 0}, // a -> b, a -> c
                {1, 0, 1, 1}, // b -> a, b -> c, b -> d
                {1, 1, 0, 1}, // c -> a, c -> b, c -> d
                {0, 1, 1, 0}  // d -> b, d -> c
        };

        HeuristicGraphColoringAlgorithm algorithm = new HeuristicGraphColoringAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм раскраски графа"));
        assertTrue(result.contains("Хроматическое число графа"));
        assertTrue(result.contains("Шаг"));
    }

    @Test
    void testEmptyGraph() {
        int[][] adjacencyMatrix = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        HeuristicGraphColoringAlgorithm algorithm = new HeuristicGraphColoringAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм раскраски графа"));
        assertTrue(result.contains("Хроматическое число графа"));
    }

    @Test
    void testSingleVertex() {
        int[][] adjacencyMatrix = {{0}};

        HeuristicGraphColoringAlgorithm algorithm = new HeuristicGraphColoringAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм раскраски графа"));
        assertTrue(result.contains("Хроматическое число графа"));
    }

    @Test
    void testCompleteGraph() {
        // Complete graph K3
        int[][] adjacencyMatrix = {
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        };

        HeuristicGraphColoringAlgorithm algorithm = new HeuristicGraphColoringAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм раскраски графа"));
        assertTrue(result.contains("Хроматическое число графа"));
    }
}
