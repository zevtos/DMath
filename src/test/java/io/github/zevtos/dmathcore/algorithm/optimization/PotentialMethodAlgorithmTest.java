package io.github.zevtos.dmathcore.algorithm.optimization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PotentialMethodAlgorithmTest {

    @Test
    void testPotentialMethod() {
        double[][] inputMatrix = {
                {2, 4, 2, 3, 8},
                {3, 5, 6, 6, 2},
                {6, 8, 7, 4, 5},
                {3, 4, 2, 1, 4}
        };

        double[] Y1 = {30, 80, 20, 30, 90};
        double[] Y2 = {120, 30, 40, 60};

        PotentialMethodData data = new PotentialMethodData(inputMatrix, Y2, Y1);
        PotentialMethodAlgorithm algorithm = new PotentialMethodAlgorithm();
        algorithm.initialize(data);
        String result = algorithm.execute();

        assertTrue(result.contains("Оптимальный план перевозок"));
        assertTrue(result.contains("Общая стоимость перевозок"));
    }
}
