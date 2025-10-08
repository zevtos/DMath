package io.github.zevtos.dmathcore.algorithm.graph;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyDoubleWeightedGraphMatrixAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BellmanFordAlgorithmTest {

    @Test
    void testBellmanFordAlgorithm() {
        // Test graph with positive weights
        int[][] adjacencyMatrix = {
                {0, 1, 1, 0},
                {1, 0, 1, 1},
                {1, 1, 0, 1},
                {0, 1, 1, 0}
        };
        double[][] weightsMatrix = {
                {Double.POSITIVE_INFINITY, 2.0, 4.0, Double.POSITIVE_INFINITY},
                {2.0, Double.POSITIVE_INFINITY, 1.0, 3.0},
                {4.0, 1.0, Double.POSITIVE_INFINITY, 2.0},
                {Double.POSITIVE_INFINITY, 3.0, 2.0, Double.POSITIVE_INFINITY}
        };

        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(adjacencyMatrix, weightsMatrix);
        BellmanFordAlgorithm algorithm = new BellmanFordAlgorithm(graph, 0, 3);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("BellmanFord"));
        assertTrue(result.contains("Матрица W"));
        assertTrue(result.contains("Таблица λ"));
        assertTrue(result.contains("Длина маршрута"));
        assertTrue(result.contains("Вес маршрута"));
        assertTrue(result.contains("Путь"));
    }

    @Test
    void testNegativeWeightCycle() {
        // Test graph with negative weight cycle
        int[][] adjacencyMatrix = {
                {0, 1, 0},
                {0, 0, 1},
                {1, 0, 0}
        };
        double[][] weightsMatrix = {
                {Double.POSITIVE_INFINITY, 1.0, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, -2.0},
                {-1.0, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}
        };

        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(adjacencyMatrix, weightsMatrix);
        BellmanFordAlgorithm algorithm = new BellmanFordAlgorithm(graph, 0, 2);
        String result = algorithm.execute();

        assertNotNull(result);
        assertEquals("Graph contains a negative weight cycle", result);
    }

    @Test
    void testSingleVertex() {
        // Test with single vertex
        int[][] adjacencyMatrix = {{0}};
        double[][] weightsMatrix = {{Double.POSITIVE_INFINITY}};

        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(adjacencyMatrix, weightsMatrix);
        BellmanFordAlgorithm algorithm = new BellmanFordAlgorithm(graph, 0, 0);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("BellmanFord"));
        assertTrue(result.contains("Матрица W"));
    }

    @Test
    void testNoPath() {
        // Test graph with no path from source to target
        int[][] adjacencyMatrix = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        double[][] weightsMatrix = {
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}
        };

        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(adjacencyMatrix, weightsMatrix);
        BellmanFordAlgorithm algorithm = new BellmanFordAlgorithm(graph, 0, 2);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("BellmanFord"));
        assertTrue(result.contains("Матрица W"));
    }

    @Test
    void testSimplePath() {
        // Test simple linear path
        int[][] adjacencyMatrix = {
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1},
                {0, 0, 0, 0}
        };
        double[][] weightsMatrix = {
                {Double.POSITIVE_INFINITY, 1.0, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 2.0, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 3.0},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}
        };

        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(adjacencyMatrix, weightsMatrix);
        BellmanFordAlgorithm algorithm = new BellmanFordAlgorithm(graph, 0, 3);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("BellmanFord"));
        assertTrue(result.contains("Матрица W"));
        assertTrue(result.contains("Таблица λ"));
        assertTrue(result.contains("Длина маршрута"));
        assertTrue(result.contains("Вес маршрута"));
        assertTrue(result.contains("Путь"));
    }
}
