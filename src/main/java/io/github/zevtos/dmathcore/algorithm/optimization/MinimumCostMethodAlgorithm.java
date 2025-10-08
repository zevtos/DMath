package io.github.zevtos.dmathcore.algorithm.optimization;

import java.util.Arrays;

public class MinimumCostMethodAlgorithm {
    private double[][] costMatrix;
    private double[][] allocationMatrix;
    private double[] supply;
    private double[] demand;
    private StringBuilder result = new StringBuilder();

    public MinimumCostMethodAlgorithm(double[][] costMatrix, double[] supply, double[] demand) {
        this.costMatrix = costMatrix;
        this.supply = supply.clone();
        this.demand = demand.clone();
        this.allocationMatrix = new double[supply.length][demand.length];
    }

    public String execute() {
        result.append("```MinimumCostMethod\n");
        result.append("Алгоритм минимальной стоимости для транспортной задачи:\n");
        result.append("1. На каждом шаге выбирается клетка с минимальной стоимостью\n");
        result.append("2. В эту клетку заносится максимально возможное количество груза\n");
        result.append("3. Соответствующая строка или столбец исключается из рассмотрения\n");
        result.append("4. Процесс повторяется до полного распределения груза\n\n");

        showInitialMatrix();
        solve();
        showFinalResult();

        result.append("```");
        return result.toString();
    }

    private void showInitialMatrix() {
        result.append("Исходная матрица стоимостей:\n");
        result.append(String.format("%3s|", ""));
        for (int j = 0; j < demand.length; j++) {
            result.append(String.format("%5s|", " " + (char) ('a' + j) + "  "));
        }
        result.append("Запасы\n");
        
        for (int i = 0; i < supply.length; i++) {
            result.append(String.format("%3s|", (i + 1) + "  "));
            for (int j = 0; j < demand.length; j++) {
                result.append(String.format("%5s|", " " + costMatrix[i][j] + "  "));
            }
            result.append(" ").append(supply[i]).append("\n");
        }
        
        result.append("Потребности|");
        for (int j = 0; j < demand.length; j++) {
            result.append(String.format("%5s|", " " + demand[j] + "  "));
        }
        result.append("\n\n");
    }

    private void solve() {
        double[] remainingSupply = supply.clone();
        double[] remainingDemand = demand.clone();
        int step = 1;

        while (hasRemainingSupply(remainingSupply) && hasRemainingDemand(remainingDemand)) {
            int[] minCostCell = findMinimumCostCell(remainingSupply, remainingDemand);
            int i = minCostCell[0];
            int j = minCostCell[1];

            if (i == -1 || j == -1) break;

            double allocation = Math.min(remainingSupply[i], remainingDemand[j]);
            allocationMatrix[i][j] = allocation;
            remainingSupply[i] -= allocation;
            remainingDemand[j] -= allocation;

            result.append("Шаг ").append(step).append(": ");
            result.append("Клетка (").append(i + 1).append(", ").append((char)('a' + j)).append(") ");
            result.append("стоимость ").append(costMatrix[i][j]).append(", ");
            result.append("размещение ").append(allocation).append("\n");

            if (remainingSupply[i] == 0) {
                result.append("  Строка ").append(i + 1).append(" исчерпана\n");
            }
            if (remainingDemand[j] == 0) {
                result.append("  Столбец ").append((char)('a' + j)).append(" исчерпан\n");
            }
            result.append("\n");

            step++;
        }
    }

    private int[] findMinimumCostCell(double[] remainingSupply, double[] remainingDemand) {
        double minCost = Double.POSITIVE_INFINITY;
        int minI = -1, minJ = -1;

        for (int i = 0; i < remainingSupply.length; i++) {
            if (remainingSupply[i] > 0) {
                for (int j = 0; j < remainingDemand.length; j++) {
                    if (remainingDemand[j] > 0 && costMatrix[i][j] < minCost) {
                        minCost = costMatrix[i][j];
                        minI = i;
                        minJ = j;
                    }
                }
            }
        }

        return new int[]{minI, minJ};
    }

    private boolean hasRemainingSupply(double[] remainingSupply) {
        return Arrays.stream(remainingSupply).anyMatch(s -> s > 0);
    }

    private boolean hasRemainingDemand(double[] remainingDemand) {
        return Arrays.stream(remainingDemand).anyMatch(d -> d > 0);
    }

    private void showFinalResult() {
        result.append("Результат распределения:\n");
        result.append(String.format("%3s|", ""));
        for (int j = 0; j < demand.length; j++) {
            result.append(String.format("%5s|", " " + (char) ('a' + j) + "  "));
        }
        result.append("\n");
        
        for (int i = 0; i < supply.length; i++) {
            result.append(String.format("%3s|", (i + 1) + "  "));
            for (int j = 0; j < demand.length; j++) {
                result.append(String.format("%5s|", " " + allocationMatrix[i][j] + "  "));
            }
            result.append("\n");
        }
        result.append("\n");

        double totalCost = calculateTotalCost();
        result.append("Общая стоимость перевозок: ").append(totalCost).append("\n");
    }

    private double calculateTotalCost() {
        double totalCost = 0;
        for (int i = 0; i < supply.length; i++) {
            for (int j = 0; j < demand.length; j++) {
                totalCost += allocationMatrix[i][j] * costMatrix[i][j];
            }
        }
        return totalCost;
    }
}
