package io.github.zevtos.dmathcore.algorithm.optimization;

import java.util.Arrays;

public class PotentialMethodAlgorithm {

    StringBuilder result = new StringBuilder();
    private double[][] costMatrix;
    private double[][] allocationMatrix;
    private double[] rows;
    private double[] columns;
    private double[] u;
    private double[] v;

    public void initialize(PotentialMethodData data) {
        this.costMatrix = data.getCostMatrix();
        this.rows = data.getSupply();
        this.columns = data.getDemand();
        this.allocationMatrix = new double[rows.length][columns.length];
        this.u = new double[rows.length];
        this.v = new double[columns.length];
        // Инициализация начального допустимого опорного плана
        initializeAllocation();
    }

    public String execute() {
        show_matrix();
        // Оптимизация плана
        optimize();
        double totalCost = calculateTotalCost();

        // Формирование результата в виде строки
        appendResultText("Оптимальный план перевозок (матрица X):\n");
        for (double[] row : allocationMatrix) {
            appendResultText(Arrays.toString(row) + "\n");
        }
        appendResultText("Общая стоимость перевозок: " + totalCost + "\n");

        return result.toString();
    }

    private void show_matrix() {
        appendResultText(String.format("%3s|", ""));
        for (int i = 0; i < columns.length; i++) {
            appendResultText(String.format("%5s|", " " + (char) ('a' + i) + "  "));
        }
        appendResultText("\n");
        for (int i = 0; i < rows.length; i++) {
            appendResultText(String.format("%3s|", (i + 1) + "  "));
            for (int j = 0; j < columns.length; j++) {
                appendResultText(String.format("%5s|", " " + allocationMatrix[i][j] + "  "));
            }
            appendResultText("\n");
        }
        appendResultText("\n");
    }

    private void initializeAllocation() {
        int i = 0, j = 0;
        while (i < rows.length && j < columns.length) {
            double allocation = Math.min(rows[i], columns[j]);
            allocationMatrix[i][j] = allocation;
            rows[i] -= allocation;
            columns[j] -= allocation;

            if (rows[i] == 0) {
                i++;
            }
            if (columns[j] == 0) {
                j++;
            }
        }
    }

    private boolean isOptimal() {
        calculatePotentials();
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < columns.length; j++) {
                if (allocationMatrix[i][j] == 0 && u[i] + v[j] > costMatrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void calculatePotentials() {
        Arrays.fill(u, Integer.MIN_VALUE);
        Arrays.fill(v, Integer.MIN_VALUE);
        u[0] = 0;

        boolean[] visitedU = new boolean[u.length];
        boolean[] visitedV = new boolean[v.length];

        while (true) {
            boolean anyChanges = false;

            for (int i = 0; i < u.length; i++) {
                if (u[i] != Integer.MIN_VALUE && !visitedU[i]) {
                    visitedU[i] = true;
                    for (int j = 0; j < v.length; j++) {
                        if (allocationMatrix[i][j] > 0 && v[j] == Integer.MIN_VALUE) {
                            v[j] = costMatrix[i][j] - u[i];
                            anyChanges = true;
                        }
                    }
                }
            }

            for (int j = 0; j < v.length; j++) {
                if (v[j] != Integer.MIN_VALUE && !visitedV[j]) {
                    visitedV[j] = true;
                    for (int i = 0; i < u.length; i++) {
                        if (allocationMatrix[i][j] > 0 && u[i] == Integer.MIN_VALUE) {
                            u[i] = costMatrix[i][j] - v[j];
                            anyChanges = true;
                        }
                    }
                }
            }

            if (!anyChanges) {
                break;
            }
        }
    }

    private void optimize() {
        while (!isOptimal()) {
            double maxPenalty = Double.NEGATIVE_INFINITY;
            int maxI = -1, maxJ = -1;

            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < columns.length; j++) {
                    if (allocationMatrix[i][j] == 0) {
                        double penalty = u[i] + v[j] - costMatrix[i][j];
                        if (penalty > maxPenalty) {
                            maxPenalty = penalty;
                            maxI = i;
                            maxJ = j;
                        }
                    }
                }
            }

            if (maxI == -1 || maxJ == -1) {
                break;
            }

            adjustAllocations(maxI, maxJ);
        }
    }

    private void adjustAllocations(int i, int j) {
        double minAllocation = Double.POSITIVE_INFINITY;
        int newI = -1, newJ = -1;
        for (int k = 0; k < rows.length; k++) {
            if (allocationMatrix[k][j] > 0) {
                if (minAllocation > allocationMatrix[k][j]) {
                    minAllocation = allocationMatrix[k][j];
                    newI = k;
                }
            }
        }

        for (int k = 0; k < columns.length; k++) {
            if (allocationMatrix[i][k] > 0) {
                if (minAllocation > allocationMatrix[i][k]) {
                    minAllocation = allocationMatrix[i][k];
                    newJ = k;
                }
            }
        }

        for (int k = 0; k < rows.length; k++) {
            if (allocationMatrix[k][j] > 0) {
                allocationMatrix[k][j] -= minAllocation;
            }
        }

        for (int k = 0; k < columns.length; k++) {
            if (allocationMatrix[i][k] > 0) {
                allocationMatrix[i][k] -= minAllocation;
            }
        }

        allocationMatrix[i][j] += minAllocation;
    }

    private double calculateTotalCost() {
        double totalCost = 0;
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < columns.length; j++) {
                totalCost += allocationMatrix[i][j] * costMatrix[i][j];
            }
        }
        return totalCost;
    }

    private void appendResultText(String text) {
        result.append(text);
        System.out.print(text);
    }
}
