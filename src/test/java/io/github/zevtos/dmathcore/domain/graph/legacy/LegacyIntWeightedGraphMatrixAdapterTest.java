package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyIntWeightedGraphMatrixAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyIntWeightedGraphMatrixAdapterTest {

    @Test
    void testAddEdge() {
        LegacyIntWeightedGraphMatrixAdapter graph = new LegacyIntWeightedGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.setWeight(0, 1, 5);
        graph.setWeight(1, 2, 10);

        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
        assertEquals(5, graph.getWeight(0, 1));
        assertEquals(10, graph.getWeight(1, 2));
    }

    @Test
    void testRemoveEdge() {
        LegacyIntWeightedGraphMatrixAdapter graph = new LegacyIntWeightedGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.setWeight(0, 1, 5);
        graph.removeEdge(0, 1);

        assertFalse(graph.hasEdge(0, 1));
        assertEquals(Integer.MAX_VALUE, graph.getWeight(0, 1));
    }

    @Test
    void testRemoveVertex() {
        LegacyIntWeightedGraphMatrixAdapter graph = new LegacyIntWeightedGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.setWeight(0, 1, 5);
        graph.setWeight(1, 2, 10);
        graph.removeVertex(1);
        graph.displayAdjacencyMatrix();
        // Теперь вершины 2 и 0 будут новыми 1 и 0
        assertFalse(graph.hasEdge(0, 1));
        assertEquals(Integer.MAX_VALUE, graph.getWeight(0, 1));

        // Проверяем, что оставшиеся вершины корректно обновлены
        assertFalse(graph.hasEdge(1, 0));
        assertEquals(Integer.MAX_VALUE, graph.getWeight(1, 0));

        // Проверка оставшихся рёбер и весов
        graph.addEdge(0, 1);
        graph.setWeight(0, 1, 15);
        assertTrue(graph.hasEdge(0, 1));
        assertEquals(15, graph.getWeight(0, 1));
    }

    @Test
    void testDisplayWeightMatrix() {
        LegacyIntWeightedGraphMatrixAdapter graph = new LegacyIntWeightedGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.setWeight(0, 1, 5);
        graph.setWeight(1, 2, 10);

        graph.displayAdjacencyMatrix();
    }

    @Test
    void testConstructorWithMatrices() {
        int[][] adjacencyMatrix = {
            {0, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
        };

        int[][] weightsMatrix = {
            {Integer.MAX_VALUE, 5, Integer.MAX_VALUE},
            {5, Integer.MAX_VALUE, 10},
            {Integer.MAX_VALUE, 10, Integer.MAX_VALUE}
        };

        LegacyIntWeightedGraphMatrixAdapter graph = new LegacyIntWeightedGraphMatrixAdapter(adjacencyMatrix, weightsMatrix);

        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
        assertEquals(5, graph.getWeight(0, 1));
        assertEquals(10, graph.getWeight(1, 2));
    }
}
