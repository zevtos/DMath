package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyIntGraphMatrixAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegacyIntGraphMatrixLegacyPortTest {

    @Test
    void testAddEdge() {
        LegacyIntGraphMatrixAdapter graph = new LegacyIntGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        assertTrue(graph.hasEdge(0, 1));
        System.out.println("testAddEdge: " + graph.hasEdge(0, 1) + " (expected: true)");
        assertTrue(graph.hasEdge(1, 2));
        System.out.println("testAddEdge: " + graph.hasEdge(1, 2) + " (expected: true)");
    }

    @Test
    void testRemoveEdge() {
        LegacyIntGraphMatrixAdapter graph = new LegacyIntGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.removeEdge(0, 1);

        assertFalse(graph.hasEdge(0, 1));
        System.out.println("testRemoveEdge: " + graph.hasEdge(0, 1) + " (expected: false)");
    }

    @Test
    void testGetNumEdges() {
        LegacyIntGraphMatrixAdapter graph = new LegacyIntGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        assertEquals(2, graph.getNumEdges());
        System.out.println("testGetNumEdges: " + graph.getNumEdges() + " (expected: 2)");
    }

    @Test
    void testHasLoop() {
        LegacyIntGraphMatrixAdapter graph = new LegacyIntGraphMatrixAdapter(3);
        graph.addLoop(0);

        assertTrue(graph.hasLoop(0));
        System.out.println("testHasLoop: " + graph.hasLoop(0) + " (expected: true)");
    }

    @Test
    void testHasPath() {
        LegacyIntGraphMatrixAdapter graph = new LegacyIntGraphMatrixAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        assertTrue(graph.hasPath(0, 2));
        System.out.println("testHasPath: " + graph.hasPath(0, 2) + " (expected: true)");
    }
}
