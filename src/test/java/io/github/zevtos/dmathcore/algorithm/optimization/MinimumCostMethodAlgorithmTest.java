package io.github.zevtos.dmathcore.algorithm.optimization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinimumCostMethodAlgorithmTest {

    @Test
    void testMinimumCostMethodAlgorithm() {
        // Test transportation problem
        double[][] costMatrix = {
                {30.0, 80.0, 10.0, 0.0, 0.0},
                {0.0, 0.0, 10.0, 20.0, 0.0},
                {0.0, 0.0, 0.0, 10.0, 30.0},
                {0.0, 0.0, 0.0, 0.0, 60.0}
        };
        double[] supply = {120.0, 30.0, 40.0, 60.0};
        double[] demand = {30.0, 80.0, 10.0, 20.0, 50.0};

        MinimumCostMethodAlgorithm algorithm = new MinimumCostMethodAlgorithm(costMatrix, supply, demand);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("MinimumCostMethod"));
        assertTrue(result.contains("Алгоритм минимальной стоимости"));
        assertTrue(result.contains("Общая стоимость перевозок"));
        assertTrue(result.contains("Шаг"));
    }

    @Test
    void testSimpleProblem() {
        double[][] costMatrix = {
                {2.0, 3.0},
                {1.0, 4.0}
        };
        double[] supply = {10.0, 15.0};
        double[] demand = {12.0, 13.0};

        MinimumCostMethodAlgorithm algorithm = new MinimumCostMethodAlgorithm(costMatrix, supply, demand);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("MinimumCostMethod"));
        assertTrue(result.contains("Общая стоимость перевозок"));
    }

    @Test
    void testSingleSupplierSingleConsumer() {
        double[][] costMatrix = {{5.0}};
        double[] supply = {10.0};
        double[] demand = {10.0};

        MinimumCostMethodAlgorithm algorithm = new MinimumCostMethodAlgorithm(costMatrix, supply, demand);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("MinimumCostMethod"));
        assertTrue(result.contains("Общая стоимость перевозок"));
    }

    @Test
    void testBalancedProblem() {
        double[][] costMatrix = {
                {1.0, 2.0, 3.0},
                {4.0, 1.0, 2.0},
                {2.0, 3.0, 1.0}
        };
        double[] supply = {20.0, 30.0, 25.0};
        double[] demand = {25.0, 30.0, 20.0};

        MinimumCostMethodAlgorithm algorithm = new MinimumCostMethodAlgorithm(costMatrix, supply, demand);
        String result = algorithm.execute();

        assertNotNull(result);
        assertTrue(result.contains("MinimumCostMethod"));
        assertTrue(result.contains("Общая стоимость перевозок"));
    }
}
