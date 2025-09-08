package io.github.zevtos.dmathcore.algorithm.graph;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyIntGraphMatrixAdapter;

import java.util.ArrayList;

public class BronKerboschAlgorithm {
    private LegacyIntGraphMatrixAdapter graph;
    private int n = 1;
    private int a_g = 0;
    private int n_g = 0;
    private StringBuilder return_text = new StringBuilder();
    
    public BronKerboschAlgorithm(int[][] adjacencyMatrix) {
        this.graph = new LegacyIntGraphMatrixAdapter(adjacencyMatrix);
    }

    public String execute() {
        return_text.append("```Алгоритм Брона Кербоша").append('\n');
        for (int i = 0; i < graph.getNumVertices(); i++) {
            graph.addEdge(i, i); // Add self-loops
        }
        ArrayList<ArrayList<Integer>> S = new ArrayList<>(1);
        ArrayList<ArrayList<Integer>> P = new ArrayList<>(1);
        ArrayList<ArrayList<Integer>> M = new ArrayList<>(1);
        P.add(new ArrayList<>());
        M.add(new ArrayList<>());
        S.add(new ArrayList<>());
        for (int i = 0; i < graph.getNumVertices(); i++) {
            P.get(0).add(i);
        }
        showStep(S, P, M);

        executeAlgorithm(S, P, M);
        return_text.append("Число внутренней устойчивости графа:").append(a_g).append("\n");
        return_text.append("Количество НВУМ:").append(n_g).append("\n");
        return_text.append("```");
        return return_text.toString();
    }

    public void executeAlgorithm(ArrayList<ArrayList<Integer>> S, ArrayList<ArrayList<Integer>> P, ArrayList<ArrayList<Integer>> M) {
        while (true) {
            int x;
            if (!P.get(P.size() - 1).isEmpty()) {
                x = P.get(P.size() - 1).get(0);
                S.add(new ArrayList<>(S.get(S.size() - 1)));
                S.get(S.size() - 1).add(x);
                P.add(subtraction(P.get(P.size() - 1), x));
                M.add(subtraction(M.get(M.size() - 1), x));
                showStep(S, P, M);
            } else {
                x = S.get(S.size() - 1).get(S.get(S.size() - 1).size() - 1);
            }

            if (!P.get(P.size() - 1).isEmpty()) {
                continue;
            }
            if (M.get(M.size() - 1).isEmpty()) {
                return_text.append("МВУМ\n");
                int size_s = S.get(S.size() - 1).size();
                if (a_g < size_s) {
                    a_g = size_s;
                    n_g = 1;
                } else if (a_g == size_s) {
                    n_g++;
                }
            }
            // Step 3
            S.remove(S.size() - 1);
            P.remove(P.size() - 1);
            M.remove(M.size() - 1);
            // Step 4
            if (!P.isEmpty()) {
                if (!P.get(P.size() - 1).isEmpty()) P.get(P.size() - 1).remove(0);
                M.get(M.size() - 1).add(x);
                if (!(S.get(S.size() - 1).isEmpty() && P.get(P.size() - 1).isEmpty())) {
                    showStep(S, P, M);
                    continue;
                }
            }
            showStep(S, P, M);
            break;
        }
    }

    public void showStep(ArrayList<ArrayList<Integer>> S, ArrayList<ArrayList<Integer>> P, ArrayList<ArrayList<Integer>> M) {
        return_text.append(n).append(" ");
        if (n < 10) {
            return_text.append(" ");
        }
        printArrayList(S);
        printArrayList(P);
        printArrayList(M);
        return_text.append("\n");
        n++;
    }

    public void printArrayList(ArrayList<ArrayList<Integer>> P) {
        int len = 12;
        return_text.append("|");
        ArrayList<Integer> last = P.get(P.size() - 1);
        if (!last.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int i = last.get(0);
            sb.append((char) ('a' + i));
            len -= 2;
            for (int a : last) {
                if (a != i) {
                    sb.append(",").append((char) ('a' + a));
                    len -= 2;
                }
            }
            return_text.append(sb);
        } else {
            return_text.append("-");
            len -= 2;
        }
        for (int j = 0; j < len; j++) {
            return_text.append(" ");
        }
        return_text.append(" ");
    }

    private ArrayList<Integer> subtraction(ArrayList<Integer> list, int x) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int vertex : list) {
            if (vertex != x && graph.hasEdge(vertex, x)) {
                result.add(vertex);
            }
        }
        return result;
    }
}
