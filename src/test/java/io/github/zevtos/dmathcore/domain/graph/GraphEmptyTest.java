package io.github.zevtos.dmathcore.domain.graph;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphEmptyTest {

    @Test
    void emptyUndirected() {
        Graph<Integer> g = Graph.empty(false);
        assertFalse(g.isDirected());
        assertEquals(0, g.getVertexCount());
        assertEquals(0, g.getEdgeCount());
        assertFalse(g.containsVertex(1));
        assertFalse(g.addVertex(1));
        assertFalse(g.removeVertex(1));
        assertFalse(g.addEdge(1, 2));
        assertFalse(g.removeEdge(1, 2));
        assertFalse(g.hasEdge(1, 2));
        assertEquals(Set.of(), g.getVertices());
        assertEquals(Set.of(), g.getNeighbors(1));
        assertEquals(0, g.degree(1));
        assertEquals(0, g.inDegree(1));
        assertEquals(0, g.outDegree(1));
    }

    @Test
    void emptyDirected() {
        Graph<Integer> g = Graph.empty(true);
        assertTrue(g.isDirected());
        assertEquals(0, g.getVertexCount());
        assertEquals(0, g.getEdgeCount());
        assertEquals(0, g.inDegree(1)); // default 0 for directed
        assertEquals(0, g.outDegree(1));
    }
}
