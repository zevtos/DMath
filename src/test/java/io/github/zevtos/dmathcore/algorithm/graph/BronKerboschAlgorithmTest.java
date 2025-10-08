package io.github.zevtos.dmathcore.algorithm.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BronKerboschAlgorithmTest {

    @Test
    void testBronKerboschAlgorithm() {
        // Test graph with cliques
        int[][] adjacencyMatrix = {
                {0, 1, 1, 0, 0},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 0},
                {0, 1, 1, 0, 1},
                {0, 0, 0, 1, 0}
        };

        BronKerboschAlgorithm algorithm = new BronKerboschAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм Брона Кербоша"));
        assertTrue(result.contains("Число внутренней устойчивости графа"));
        assertTrue(result.contains("Количество НВУМ"));
        assertTrue(result.contains("МВУМ"));
    }

    @Test
    void testCompleteGraph() {
        // Test complete graph (all vertices connected)
        int[][] adjacencyMatrix = {
                {0, 1, 1, 1},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {1, 1, 1, 0}
        };

        BronKerboschAlgorithm algorithm = new BronKerboschAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм Брона Кербоша"));
        assertTrue(result.contains("Число внутренней устойчивости графа"));
        assertTrue(result.contains("Количество НВУМ"));
    }

    @Test
    void testEmptyGraph() {
        // Test empty graph (no edges)
        int[][] adjacencyMatrix = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        BronKerboschAlgorithm algorithm = new BronKerboschAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм Брона Кербоша"));
        assertTrue(result.contains("Число внутренней устойчивости графа"));
        assertTrue(result.contains("Количество НВУМ"));
    }

    @Test
    void testSingleVertex() {
        // Test single vertex
        int[][] adjacencyMatrix = {{0}};

        BronKerboschAlgorithm algorithm = new BronKerboschAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм Брона Кербоша"));
        assertTrue(result.contains("Число внутренней устойчивости графа"));
        assertTrue(result.contains("Количество НВУМ"));
    }

    @Test
    void testPathGraph() {
        // Test path graph (linear chain)
        int[][] adjacencyMatrix = {
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {0, 0, 1, 0}
        };

        BronKerboschAlgorithm algorithm = new BronKerboschAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм Брона Кербоша"));
        assertTrue(result.contains("Число внутренней устойчивости графа"));
        assertTrue(result.contains("Количество НВУМ"));
    }

    @Test
    void testStarGraph() {
        // Test star graph (one central vertex connected to all others)
        int[][] adjacencyMatrix = {
                {0, 1, 1, 1},
                {1, 0, 0, 0},
                {1, 0, 0, 0},
                {1, 0, 0, 0}
        };

        BronKerboschAlgorithm algorithm = new BronKerboschAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Алгоритм Брона Кербоша"));
        assertTrue(result.contains("Число внутренней устойчивости графа"));
        assertTrue(result.contains("Количество НВУМ"));
    }
}
