package io.github.zevtos.dmathcore.algorithm.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimAlgorithmTest {

    @Test
    void testPrimAlgorithm() {
        // Test graph from the original main method
        double[][] weightsMatrix = {
                {Double.POSITIVE_INFINITY, 4, Double.POSITIVE_INFINITY, 16, 15, Double.POSITIVE_INFINITY},
                {4, Double.POSITIVE_INFINITY, 4, 4, Double.POSITIVE_INFINITY, 4},
                {Double.POSITIVE_INFINITY, 4, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 7},
                {16, 4, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 6, 5},
                {15, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 6, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, 4, 7, 5, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}
        };

        PrimAlgorithm algorithm = new PrimAlgorithm(weightsMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("PrimAlgorithm"));
        assertTrue(result.contains("MST"));
        assertTrue(result.contains("Суммарный вес рёбер MOD"));
        assertTrue(result.contains("Ребро"));
    }

    @Test
    void testEmptyGraph() {
        // Test with a graph that has no edges (all weights are infinity)
        double[][] weightsMatrix = {
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}
        };

        PrimAlgorithm algorithm = new PrimAlgorithm(weightsMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("PrimAlgorithm"));
        assertTrue(result.contains("MST"));
    }

    @Test
    void testSingleVertex() {
        double[][] weightsMatrix = {{Double.POSITIVE_INFINITY}};

        PrimAlgorithm algorithm = new PrimAlgorithm(weightsMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("PrimAlgorithm"));
        assertTrue(result.contains("MST"));
    }

    @Test
    void testSimpleGraph() {
        double[][] weightsMatrix = {
                {Double.POSITIVE_INFINITY, 5, 3},
                {5, Double.POSITIVE_INFINITY, 1},
                {3, 1, Double.POSITIVE_INFINITY}
        };

        PrimAlgorithm algorithm = new PrimAlgorithm(weightsMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("PrimAlgorithm"));
        assertTrue(result.contains("MST"));
        assertTrue(result.contains("Суммарный вес рёбер MOD"));
    }
}
