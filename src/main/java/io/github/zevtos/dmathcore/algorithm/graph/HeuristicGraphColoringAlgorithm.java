package io.github.zevtos.dmathcore.algorithm.graph;

import io.github.zevtos.dmathcore.domain.graph.legacy.LegacyColoredGraphMatrixAdapter;

import java.util.ArrayList;

public class HeuristicGraphColoringAlgorithm {
    private int colorNum = 1;
    private StringBuilder return_text = new StringBuilder();
    private int[][] adjacencyMatrix;

    public HeuristicGraphColoringAlgorithm(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public String execute() {
        return colorGraph(adjacencyMatrix);
    }

    public String colorGraph(int[][] adjacencyMatrix) {
        return_text.append("```Алгоритм раскраски графа").append('\n');
        LegacyColoredGraphMatrixAdapter graph = new LegacyColoredGraphMatrixAdapter(adjacencyMatrix);
        int numVert = graph.getNumVertices();

        // Установка -1 на диагонали матрицы смежности
        for (int i = 0; i < numVert; i++) {
            graph.set(i, i, -1);
        }
        return_text.append(graph.adjacencyMatrixToString()).append('\n');
ч
        int[] degrees = graph.getDegrees();
        displayDegrees(degrees);
        displayHeader(numVert);

        ArrayList<Integer> maxVertices;
        ArrayList<ArrayList<Integer>> tableOfVectors;

        while (graph.containsColor(0)) {
            return_text.append("Шаг ").append(colorNum).append("\n");

            degrees = graph.getDegrees();
            int maxDegreeVertex = graph.getMaxDegreeVertex(degrees);
            maxVertices = new ArrayList<>();
            maxVertices.add(maxDegreeVertex);

            tableOfVectors = new ArrayList<>();
            tableOfVectors.add(new ArrayList<>());
            for (int c : graph.getLine(maxDegreeVertex)) {
                tableOfVectors.get(0).add(c);
            }

            if (colorNum > 1) {
                return_text.append("Необходимо удалить все вершины, полученные на предыдущем шаге, в таблице векторов:\n");
                displayDegrees(degrees);
            }
            displayTable(maxVertices, tableOfVectors);

            while (tableOfVectors.get(tableOfVectors.size() - 1).contains(0)) {
                maxDegreeVertex = LegacyColoredGraphMatrixAdapter.getMaxDegreeVertex(degrees, tableOfVectors.get(tableOfVectors.size() - 1));
                maxVertices.add(maxDegreeVertex);
                ArrayList<Integer> newVector = new ArrayList<>(tableOfVectors.get(tableOfVectors.size() - 1));
                newVector.set(maxDegreeVertex, -1);
                for (int c : graph.getNeighbors(maxDegreeVertex)) {
                    if (newVector.get(c) != -1) newVector.set(c, 1);
                }
                tableOfVectors.add(newVector);
                displayTable(maxVertices, tableOfVectors);
            }

            for (int i : maxVertices) {
                graph.setColor(i, colorNum);
                graph.removeRowAndColumn(i);
            }
            colorNum++;
        }
        return_text.append("Хроматическое число графа: ").append(colorNum - 1).append("\n");
        return_text.append("```");
        return return_text.toString();
    }

    private void displayDegrees(int[] degrees) {
        return_text.append("  p\n");
        int i = 0;
        for (int degree : degrees) {
            return_text.append((char) ('a' + i++)).append(" ");
            if (degree == -1) {
                return_text.append("-\n");
            } else {
                return_text.append(degree).append("\n");
            }
        }
    }

    private void displayHeader(int numVert) {
        return_text.append("|").append(String.format("%15s", "X'/xi")).append(String.format("%10s|", ""));
        for (int i = 0; i < numVert; i++) {
            return_text.append(String.format("%4s", (char) ('a' + i))).append(String.format("%3s|", ""));
        }
        return_text.append("\n");
    }

    private void displayTable(ArrayList<Integer> maxVertices, ArrayList<ArrayList<Integer>> tableOfVectors) {
        return_text.append("|");
        StringBuilder text = new StringBuilder();
        text.append("{");
        for (int x : maxVertices) {
            text.append((char) ('a' + x)).append(",");
        }
        text.deleteCharAt(text.length() - 1).append("}");
        return_text.append(String.format("%21s", text)).append("|");
        for (int i : tableOfVectors.get(tableOfVectors.size() - 1)) {
            if (i == -1) {
                return_text.append("  -  ");
            } else {
                return_text.append("  ").append(i).append("  ");
            }
            return_text.append("|");
        }
        return_text.append("\n");
    }
}
