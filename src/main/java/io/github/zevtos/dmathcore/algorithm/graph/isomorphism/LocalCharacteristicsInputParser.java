package io.github.zevtos.dmathcore.algorithm.graph.isomorphism;

import java.util.ArrayList;
import java.util.List;

class LocalCharacteristicsInputParser {

    int[][] parseAdjacencyList(List<String> inputData) {
        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (String row : inputData) {
            List<Integer> neighbors = new ArrayList<>();
            String s = row == null ? "" : row.trim();
            if (!s.equals("0") && !s.equalsIgnoreCase("none")) {
                for (char ch : s.toCharArray()) {
                    if (Character.isDigit(ch)) {
                        neighbors.add(Character.getNumericValue(ch));
                    }
                }
            }
            adjacencyList.add(neighbors);
        }

        int n = adjacencyList.size();
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j : adjacencyList.get(i)) {
                if (j >= 1 && j <= n) {
                    result[i][j - 1] += 1;
                }
            }
        }
        return result;
    }
}
