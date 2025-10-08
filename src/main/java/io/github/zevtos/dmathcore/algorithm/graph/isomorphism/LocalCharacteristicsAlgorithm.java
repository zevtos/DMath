package io.github.zevtos.dmathcore.algorithm.graph.isomorphism;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LocalCharacteristicsAlgorithm {
    private int[][] arcsG;
    private int[][] edgesG;
    private int[][] arcsH;
    private int[][] edgesH;
    private int n;
    private StringBuilder return_text;
    private int[][] P;

    public void initialize(Object input) {
        if (!(input instanceof int[][][])) {
            throw new IllegalArgumentException("Input should be an array of four matrices.");
        }
        int[][][] matrices = (int[][][]) input;
        this.arcsG = matrices[0];
        this.edgesG = matrices[1];
        this.arcsH = matrices[2];
        this.edgesH = matrices[3];
        this.n = arcsG.length;
        this.return_text = new StringBuilder();
        this.P = new int[2][n];
    }

    public String execute() {
        return_text.append("```Алгоритм Локальных Характеристик").append('\n');
        int[][] S0G = calculateS0(arcsG, edgesG);
        int[][] S0H = calculateS0(arcsH, edgesH);

        calculateP0(S0G, S0H);

        generateOutput("S0(G)", S0G, P[0]);
        generateOutput("S0(H)", S0H, P[1]);

        for (int k = 1; ; k++) {
            int[][] SkG = calculateSk(S0G, P[0], k);
            int[][] SkH = calculateSk(S0H, P[1], k);

            calculatePk(SkG, SkH);

            generateOutput("S" + k + "(G)", SkG, P[0]);
            generateOutput("S" + k + "(H)", SkH, P[1]);
            int maxValue = Integer.MIN_VALUE;
            for (int[] row : P) {
                for (int value : row) {
                    if (value > maxValue) {
                        maxValue = value;
                    }
                }
            }
            if (maxValue < n) continue;
            Map<Integer, Integer> frequencyMap1 = getFrequencyMap(P[0]);
            Map<Integer, Integer> frequencyMap2 = getFrequencyMap(P[1]);
            if (frequencyMap1.equals(frequencyMap2)) {
                return_text.append("Graphs are isomorphic.\n");
                int[] ans = new int[n];
                for (int i = 0; i < n; i++) {
                    ans[P[1][i] - 1] = i + 1;
                }
                return_text.append("Mapping: ").append(Arrays.toString(ans)).append("\n");
            } else {
                return_text.append("Graphs are not isomorphic.\n");
            }
            break;
        }
        return_text.append("```");
        return return_text.toString();
    }

    private Map<Integer, Integer> getFrequencyMap(int[] array) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int value : array) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }
        return frequencyMap;
    }

    private int[][] calculateS0(int[][] arcs, int[][] edges) {
        int[][] S0 = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    S0[i][j] = arcs[i][j] * 10 + edges[i][j];
                } else {
                    S0[i][j] = Math.max(edges[i][j], arcs[i][j]);
                }
            }
        }
        return S0;
    }

    private void calculateP0(int[][] S0G, int[][] S0H) {
        Map<Map<Integer, Integer>, Integer> uniquePatterns = new HashMap<>();
        int code = 1;

        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int j = 0; j < n; j++) {
                frequencyMap.put(S0G[i][j], frequencyMap.getOrDefault(S0G[i][j], 0) + 1);
            }

            if (!uniquePatterns.containsKey(frequencyMap)) {
                uniquePatterns.put(frequencyMap, code++);
            }
            P[0][i] = uniquePatterns.get(frequencyMap);
        }

        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int j = 0; j < n; j++) {
                frequencyMap.put(S0H[i][j], frequencyMap.getOrDefault(S0H[i][j], 0) + 1);
            }

            if (!uniquePatterns.containsKey(frequencyMap)) {
                uniquePatterns.put(frequencyMap, code++);
            }
            P[1][i] = uniquePatterns.get(frequencyMap);
        }
    }

    private int[][] calculateSk(int[][] S0, int[] P, int k) {
        int[][] Sk = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (S0[i][j] == 0) continue;
                Sk[i][j] = S0[i][j] * 100 + P[i] * 10 + P[j];
            }
        }
        return Sk;
    }

    private void calculatePk(int[][] SkG, int[][] SkH) {
        Map<Map<Integer, Integer>, Integer> uniquePatterns = new HashMap<>();
        int code = 1;

        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int j = 0; j < n; j++) {
                frequencyMap.put(SkG[i][j], frequencyMap.getOrDefault(SkG[i][j], 0) + 1);
            }

            if (!uniquePatterns.containsKey(frequencyMap)) {
                uniquePatterns.put(frequencyMap, code++);
            }
            P[0][i] = uniquePatterns.get(frequencyMap);
        }

        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (int j = 0; j < n; j++) {
                frequencyMap.put(SkH[i][j], frequencyMap.getOrDefault(SkH[i][j], 0) + 1);
            }

            if (!uniquePatterns.containsKey(frequencyMap)) {
                uniquePatterns.put(frequencyMap, code++);
            }
            P[1][i] = uniquePatterns.get(frequencyMap);
        }
    }

    private void generateOutput(String label, int[][] matrix, int[] P) {
        return_text.append(label).append(":\n");
        return_text.append("   ");
        for (int i = 0; i < n; i++) {
            return_text.append(String.format("%6d", i + 1));
        }
        return_text.append("\n");
        for (int i = 0; i < n; i++) {
            return_text.append(String.format("%2d", i + 1)).append(" ");
            for (int j = 0; j < n; j++) {
                return_text.append(String.format("%6d", matrix[i][j]));
            }
            return_text.append("  ").append(P[i]).append("\n");
        }
        return_text.append("\n");
    }
}
