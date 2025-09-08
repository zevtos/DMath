package io.github.zevtos.dmathcore.domain.graph.legacy;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyColoredGraphMatrixAdapter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LegacyColoredGraphMatrixAdapterTest {

    @Test
    void colorSetGetAndContains() {
        LegacyColoredGraphMatrixAdapter g = new LegacyColoredGraphMatrixAdapter(3);
        g.setColor(0, 1);
        assertEquals(1, g.getColor(0));
        assertTrue(g.isColored(0));
        assertTrue(g.containsColor(1));
        assertFalse(g.containsColor(2));
    }

    @Test
    void degreesAndMaxDegreeSelection() {
        LegacyColoredGraphMatrixAdapter g = new LegacyColoredGraphMatrixAdapter(4);
        int[][] adj = g.getAdjacencyMatrix();
        // Make a star centered at 0
        adj[0][1] = adj[1][0] = 1;
        adj[0][2] = adj[2][0] = 1;
        adj[0][3] = adj[3][0] = 1;
        int[] degrees = g.getDegrees();
        int idx = g.getMaxDegreeVertex(degrees);
        assertEquals(0, idx);
        boolean[] acces = new boolean[]{false, true, true, true};
        assertEquals(0, LegacyColoredGraphMatrixAdapter.getMaxDegreeVertex(degrees, acces));
        ArrayList<Integer> accesList = new ArrayList<>();
        accesList.add(0); accesList.add(1); accesList.add(1); accesList.add(1);
        assertEquals(0, LegacyColoredGraphMatrixAdapter.getMaxDegreeVertex(degrees, accesList));
    }

    @Test
    void colorGraphRemovesRows() {
        LegacyColoredGraphMatrixAdapter g = new LegacyColoredGraphMatrixAdapter(3);
        int[][] adj = g.getAdjacencyMatrix();
        adj[0][1] = adj[1][0] = 1;
        adj[1][2] = adj[2][1] = 1;
        g.colorGraph();
        // After coloring, some rows/columns should be marked -1
        boolean anyMinus = false;
        for (int i = 0; i < g.getNumVertices(); i++) {
            for (int j = 0; j < g.getNumVertices(); j++) {
                if (adj[i][j] == -1) anyMinus = true;
            }
        }
        assertTrue(anyMinus);
    }

    @Test
    void removeVertexShrinksColors() {
        LegacyColoredGraphMatrixAdapter g = new LegacyColoredGraphMatrixAdapter(3);
        g.setColor(0, 1);
        g.removeVertex(2);
        assertEquals(2, g.getNumVertices());
    }
}
