package io.github.zevtos.dmathcore.domain.graph;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {

    @Test
    void isConnectedUndirected() {
        Graph<Integer> g = new AdjacencyListGraph<>(false);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        assertTrue(GraphAlgorithms.isConnected(g));
        Graph<Integer> g2 = new AdjacencyListGraph<>(false);
        g2.addEdge(0, 1);
        g2.addVertex(2); // isolated vertex makes it disconnected
        assertFalse(GraphAlgorithms.isConnected(g2));
    }

    @Test
    void isConnectedDirectedWeakConnectivity() {
        Graph<Integer> g = new AdjacencyListGraph<>(true);
        // 0 -> 1, and 2 -> 1, weakly connected as undirected
        g.addEdge(0, 1);
        g.addEdge(2, 1);
        assertTrue(GraphAlgorithms.isConnected(g));

        Graph<Integer> g2 = new AdjacencyListGraph<>(true);
        g2.addVertex(0);
        g2.addVertex(2);
        assertFalse(GraphAlgorithms.isConnected(g2));
    }

    @Test
    void connectedComponents() {
        Graph<Integer> g = new AdjacencyListGraph<>(false);
        g.addEdge(0, 1);
        g.addEdge(2, 3);
        List<Set<Integer>> comps = GraphAlgorithms.connectedComponents(g);
        assertEquals(2, comps.size());
        Set<Integer> merged = new HashSet<>();
        comps.forEach(merged::addAll);
        assertEquals(Set.of(0,1,2,3), merged);
    }

    @Test
    void shortestPathBfs() {
        Graph<Integer> g = new AdjacencyListGraph<>(false);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        assertEquals(List.of(0,1,2,3), GraphAlgorithms.shortestPathBfs(g, 0, 3));

        Graph<Integer> d = new AdjacencyListGraph<>(true);
        d.addEdge(0, 1);
        d.addEdge(1, 2);
        assertEquals(Collections.emptyList(), GraphAlgorithms.shortestPathBfs(d, 2, 0));
    }

    @Test
    void hasBridgesUndirected() {
        Graph<Integer> g = new AdjacencyListGraph<>(false);
        // Triangle 0-1-2-0 has no bridges
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        assertFalse(GraphAlgorithms.hasBridgesUndirected(g));

        Graph<Integer> g2 = new AdjacencyListGraph<>(false);
        // Chain 0-1-2 has bridges (0-1) and (1-2)
        g2.addEdge(0, 1);
        g2.addEdge(1, 2);
        assertTrue(GraphAlgorithms.hasBridgesUndirected(g2));
    }
}
