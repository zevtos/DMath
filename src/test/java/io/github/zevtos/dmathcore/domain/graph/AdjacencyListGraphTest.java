package io.github.zevtos.dmathcore.domain.graph;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {

    @Test
    void addVertexAndDuplicate() {
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph<>(false);
        assertTrue(g.addVertex(1));
        assertFalse(g.addVertex(1));
        assertEquals(1, g.getVertexCount());
        assertTrue(g.getVertices().contains(1));
        assertThrows(UnsupportedOperationException.class, () -> g.getVertices().add(2));
    }

    @Test
    void addEdgeAutoAddsVerticesUndirectedAndCounts() {
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph<>(false);
        assertTrue(g.addEdge(0, 1));
        assertFalse(g.addEdge(0, 1)); // duplicate
        assertEquals(2, g.getVertexCount());
        assertEquals(1, g.getEdgeCount());
        assertTrue(g.hasEdge(0, 1));
        assertTrue(g.hasEdge(1, 0)); // mirrored
        assertEquals(Set.of(1), g.getNeighbors(0));
        assertEquals(Set.of(0), g.getNeighbors(1));
        assertThrows(UnsupportedOperationException.class, () -> g.getNeighbors(0).add(2));
    }

    @Test
    void addEdgeAutoAddsVerticesDirectedAndCounts() {
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph<>(true);
        assertTrue(g.addEdge(0, 1));
        assertEquals(2, g.getVertexCount());
        assertEquals(1, g.getEdgeCount());
        assertTrue(g.hasEdge(0, 1));
        assertFalse(g.hasEdge(1, 0));
        assertEquals(1, g.outDegree(0));
        assertEquals(1, g.inDegree(1));
        assertEquals(0, g.inDegree(0));
    }

    @Test
    void selfLoopCountsAndDegrees() {
        AdjacencyListGraph<Integer> gUndir = new AdjacencyListGraph<>(false);
        assertTrue(gUndir.addEdge(2, 2));
        assertEquals(1, gUndir.getEdgeCount());
        assertTrue(gUndir.hasEdge(2, 2));
        assertEquals(1, gUndir.degree(2));

        AdjacencyListGraph<Integer> gDir = new AdjacencyListGraph<>(true);
        assertTrue(gDir.addEdge(3, 3));
        assertEquals(1, gDir.getEdgeCount());
        assertEquals(1, gDir.inDegree(3));
        assertEquals(1, gDir.outDegree(3));
    }

    @Test
    void removeEdgeBehaviors() {
        AdjacencyListGraph<Integer> gUndir = new AdjacencyListGraph<>(false);
        gUndir.addEdge(0, 1);
        assertTrue(gUndir.removeEdge(0, 1));
        assertFalse(gUndir.hasEdge(0, 1));
        assertFalse(gUndir.hasEdge(1, 0));
        assertEquals(0, gUndir.getEdgeCount());
        assertFalse(gUndir.removeEdge(0, 1)); // already removed

        AdjacencyListGraph<Integer> gDir = new AdjacencyListGraph<>(true);
        gDir.addEdge(0, 1);
        assertTrue(gDir.removeEdge(0, 1));
        assertFalse(gDir.hasEdge(0, 1));
        assertEquals(0, gDir.getEdgeCount());
        assertFalse(gDir.removeEdge(0, 1));
    }

    @Test
    void removeVertexUndirectedDecrementsOncePerIncidentEdge() {
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph<>(false);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        assertEquals(2, g.getEdgeCount());
        assertTrue(g.removeVertex(1));
        assertEquals(0, g.getEdgeCount());
        assertFalse(g.hasEdge(0, 1));
        assertFalse(g.hasEdge(2, 1));
        assertFalse(g.removeVertex(1)); // already removed
    }

    @Test
    void removeVertexDirectedRemovesIncomingAndOutgoing() {
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph<>(true);
        g.addEdge(0, 1); // incoming to 1
        g.addEdge(1, 2); // outgoing from 1
        assertEquals(2, g.getEdgeCount());
        assertTrue(g.removeVertex(1));
        assertEquals(0, g.getEdgeCount());
        assertFalse(g.hasEdge(0, 1));
        assertFalse(g.hasEdge(1, 2));
    }

    @Test
    void neighborsOfUnknownVertexIsEmptyView() {
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph<>(false);
        assertTrue(g.getNeighbors(42).isEmpty());
    }
}
