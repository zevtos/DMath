package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyColoredGraphMatrixAdapter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LegacyColoredGraphMatrixLegacyPortTest {

    @Test
    void testSetColor() {
        LegacyColoredGraphMatrixAdapter graph = new LegacyColoredGraphMatrixAdapter(3);
        graph.setColor(0, 1);
        assertEquals(1, graph.getColor(0));
    }

    @Test
    void testIsColored() {
        LegacyColoredGraphMatrixAdapter graph = new LegacyColoredGraphMatrixAdapter(3);
        graph.setColor(0, 1);
        assertTrue(graph.isColored(0));
        assertFalse(graph.isColored(1));
    }

    @Test
    void testGetMaxDegreeVertex() {
        @SuppressWarnings("unchecked")
        Set<Integer>[] graphData = new HashSet[3];
        for (int i = 0; i < 3; i++) graphData[i] = new HashSet<>();
        graphData[0].add(1);
        graphData[1].add(0);
        graphData[1].add(2);
        graphData[2].add(1);

        LegacyColoredGraphMatrixAdapter graph = new LegacyColoredGraphMatrixAdapter(graphData);
        int maxDegreeVertex = graph.getMaxDegreeVertex();
        assertEquals(1, maxDegreeVertex);
    }

    @Test
    void testColorGraph() {
        @SuppressWarnings("unchecked")
        Set<Integer>[] graphData = new HashSet[3];
        for (int i = 0; i < 3; i++) graphData[i] = new HashSet<>();
        graphData[0].add(1);
        graphData[1].add(0);
        graphData[1].add(2);
        graphData[2].add(1);

        LegacyColoredGraphMatrixAdapter graph = new LegacyColoredGraphMatrixAdapter(graphData);
        graph.colorGraph();
        assertTrue(graph.isColored(0));
        assertTrue(graph.isColored(1));
        assertTrue(graph.isColored(2));
    }
}
