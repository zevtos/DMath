package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyDoubleWeightedGraphMatrixAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyDoubleWeightedGraphMatrixAdapterTest {

    @Test
    void testAddEdgeAndWeights() {
        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.setWeight(0, 1, 5.5);
        graph.setWeight(1, 2, 10.25);
        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
        assertEquals(5.5, graph.getWeight(0, 1));
        assertEquals(10.25, graph.getWeight(1, 2));
    }

    @Test
    void testRemoveEdgeResetsToInf() {
        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.setWeight(0, 1, 1.0);
        graph.removeEdge(0, 1);
        assertFalse(graph.hasEdge(0, 1));
        assertTrue(Double.isInfinite(graph.getWeight(0, 1)));
    }

    @Test
    void testRemoveVertexShrinksWeights() {
        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.setWeight(0, 1, 3.0);
        graph.setWeight(1, 2, 4.0);
        graph.removeVertex(1);
        assertEquals(2, graph.getNumVertices());
        assertTrue(Double.isInfinite(graph.getWeight(0, 1)));
        graph.addEdge(0, 1);
        graph.setWeight(0, 1, 7.0);
        assertEquals(7.0, graph.getWeight(0, 1));
    }

    @Test
    void testCustomDisplayAndHelpers() {
        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(2);
        graph.addEdge(0, 1);
        graph.setWeight(0, 1, 2.5);
        graph.customDisplayAdjacencyMatrix(new java.util.HashMap<>());
        assertTrue(graph.getZeros().isEmpty());
        double[][] copy = graph.copyWeightMatrix();
        assertEquals(2, copy.length);
        graph.subtraction(0, 1, 1.0);
        assertEquals(1.5, graph.getWeight(0, 1));
    }

    @Test
    void testDisplayMatrixsAndAdjacencyString() {
        LegacyDoubleWeightedGraphMatrixAdapter graph = new LegacyDoubleWeightedGraphMatrixAdapter(2);
        graph.addEdge(0, 1);
        graph.setWeight(0, 1, 1.0);
        String s1 = graph.displayMatrixs(new java.util.HashMap<>(), "*");
        String s2 = graph.adjacencyMatrixToString();
        assertTrue(s1.contains("Матрица смежности"));
        assertTrue(s2.contains("Матрица смежности с весами"));
    }
}
