package io.github.zevtos.dmathcore.domain.graph.legacy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LegacyIntGraphAdapterTest {

    @Test
    void testAddEdge() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
    }

    @Test
    void testRemoveEdge() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(3);
        graph.addEdge(0, 1);
        graph.removeEdge(0, 1);

        assertFalse(graph.hasEdge(0, 1));
    }

    @Test
    void testGetDegree() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);

        assertEquals(2, graph.getDegree(0));
    }

    @Test
    void testGetHighDegreeVertex() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);

        assertEquals(0, graph.getHighDegreeVertex());
    }

    @Test
    void testIsConnected() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        assertTrue(graph.isConnected());

        LegacyIntGraphAdapter graph2 = new LegacyIntGraphAdapter(3);
        graph2.addEdge(0, 1);

        assertFalse(graph2.isConnected());
    }

    @Test
    void testGetConnectedComponents() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(4);
        graph.addEdge(0, 1);
        graph.addEdge(2, 3);

        List<Set<Integer>> components = graph.getConnectedComponents();
        assertEquals(2, components.size());
    }

    @Test
    void testShortestPath() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        List<Integer> path = graph.shortestPath(0, 3);
        List<Integer> expectedPath = Arrays.asList(0, 1, 2, 3);
        assertEquals(expectedPath, path);
    }

    // Optional: new tests leveraging added API
    @Test
    void testLoopsAndAdjSnapshot() {
        LegacyIntGraphAdapter graph = new LegacyIntGraphAdapter(3);
        graph.addLoop(1);
        assertTrue(graph.hasLoop(1));
        Set<Integer>[] adj = graph.getAdj();
        assertEquals(3, adj.length);
        assertTrue(adj[1].contains(1));
    }
}
