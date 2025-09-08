package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyIntGraphAdapter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LegacyIntGraphAdapterExtrasTest {

    @Test
    void createFromAdjacencyConstructorAndGetAdj() {
        @SuppressWarnings("unchecked")
        Set<Integer>[] adj = new HashSet[3];
        for (int i = 0; i < 3; i++) adj[i] = new HashSet<>();
        adj[0].add(1);
        adj[1].add(2);
        LegacyIntGraphAdapter g = new LegacyIntGraphAdapter(adj);
        assertEquals(3, g.getNumVertices());
        Set<Integer>[] snapshot = g.getAdj();
        assertEquals(3, snapshot.length);
        assertTrue(snapshot[0].contains(1));
        assertTrue(snapshot[1].contains(2));
    }

    @Test
    void createCompleteGraphHelper() {
        Set<Integer>[] comp = LegacyIntGraphAdapter.createCompleteGraph(3);
        assertEquals(3, comp.length);
        assertEquals(Set.of(1,2), comp[0]);
        assertEquals(Set.of(0,2), comp[1]);
        assertEquals(Set.of(0,1), comp[2]);
    }

    @Test
    void subtractionAndIsValidVertexAndPrintGraph() {
        LegacyIntGraphAdapter g = new LegacyIntGraphAdapter(3);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        ArrayList<Integer> list = new ArrayList<>(List.of(0,1,2));
        ArrayList<Integer> res = g.subtraction(list, 0);
        assertEquals(List.of(0), res);
        assertTrue(g.isValidVertex(2));
        assertFalse(g.isValidVertex(3));
        g.printGraph(); // smoke test, no exception
    }

    @Test
    void hasBridgesDirectedReturnsFalse() {
        LegacyIntGraphAdapter g = new LegacyIntGraphAdapter(true, 3);
        g.addEdge(0, 1);
        assertFalse(g.hasBridges());
    }
}
