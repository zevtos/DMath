package io.github.zevtos.dmathcore.algorithm.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GammaAlgorithmTest {

    @Test
    void testGammaAlgorithm() {
        int[][] adjacencyMatrix = {
                //a  b  c  d  e  f  g  h  i  j
                { 0, 1, 0, 0, 0, 0, 0, 0, 0, 1 }, // a
                { 1, 0, 1, 0, 0, 0, 0, 0, 1, 0 }, // b
                { 0, 1, 0, 1, 0, 0, 0, 0, 0, 0 }, // c
                { 0, 0, 1, 0, 1, 0, 1, 0, 0, 0 }, // d
                { 0, 0, 0, 1, 0, 1, 0, 1, 0, 0 }, // e
                { 0, 0, 0, 0, 1, 0, 1, 1, 0, 0 }, // f
                { 0, 0, 0, 1, 0, 1, 0, 1, 0, 0 }, // g
                { 0, 0, 0, 0, 1, 1, 1, 0, 1, 0 }, // h
                { 0, 1, 0, 0, 0, 0, 0, 1, 0, 1 }, // i
                { 1, 0, 0, 0, 0, 0, 0, 0, 1, 0 }  // j
        };

        GammaAlgorithm algorithm = new GammaAlgorithm(adjacencyMatrix);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("Граф") || result.contains("цикл") || result.contains("сегмент"));
    }
}
