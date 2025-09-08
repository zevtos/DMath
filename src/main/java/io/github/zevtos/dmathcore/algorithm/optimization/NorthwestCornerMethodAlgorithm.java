package io.github.zevtos.dmathcore.algorithm.optimization;

import java.util.Arrays;

public class NorthwestCornerMethodAlgorithm {
    private double[][] costMatrix;
    private double[][] allocationMatrix;
    private double[] supply;
    private double[] demand;
    private StringBuilder result = new StringBuilder();

    public NorthwestCornerMethodAlgorithm(double[][] costMatrix, double[] supply, double[] demand) {
        this.costMatrix = costMatrix;
        this.supply = supply.clone();
        this.demand = demand.clone();
        this.allocationMatrix = new double[supply.length][demand.length];
    }

    public String execute() {
        result.append("```NorthwestCornerMethod\n");
        result.append("Алгоритм северо-западного угла для транспортной задачи:\n");
        result.append("1. Начинаем с северо-западного угла (левый верхний)\n");
        result.append("2. Размещаем максимально возможное количество груза\n");
        result.append("3. Переходим к следующей клетке вправо или вниз\n");
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
        int i = 0, j = 0;
        int step = 1;

        while (i < supply.length && j < demand.length) {
            double allocation = Math.min(remainingSupply[i], remainingDemand[j]);
            allocationMatrix[i][j] = allocation;
            remainingSupply[i] -= allocation;
            remainingDemand[j] -= allocation;

            result.append("Шаг ").append(step).append(": ");
            result.append("Клетка (").append(i + 1).append(", ").append((char)('a' + j)).append(") ");
            result.append("стоимость ").append(costMatrix[i][j]).append(", ");
            result.append("размещение ").append(allocation).append("\n");

            if (remainingSupply[i] == 0) {
                result.append("  Строка ").append(i + 1).append(" исчерпана, переходим вниз\n");
                i++;
            } else {
                result.append("  Столбец ").append((char)('a' + j)).append(" исчерпан, переходим вправо\n");
                j++;
            }
            result.append("\n");

            step++;
        }
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
