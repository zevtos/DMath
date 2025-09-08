package io.github.zevtos.dmathcore.algorithm.nn;

import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;

import static org.junit.jupiter.api.Assertions.*;

class CNNPropagationAlgorithmTest {

    @Test
    void smokeRun() throws Exception {
        double[][] inputMatrix = {
                {0.15, 0.36, 0, 0.28, 0.57, 0.62, 0.87},
                {0.49, 0.84, 0.51, 0.48, 0.64, 0.2, 0.43},
                {0.74, 0, 0.75, 0.46, 0.23, 0.52, 0.64},
                {0.37, 0.67, 0.62, 0.35, 0.92, 0.68, 0.34},
                {0.13, 0.98, 0.26, 0.94, 0.08, 0.42, 0.34},
                {0.38, 0.73, 0.17, 0.56, 0.64, 0.89, 0.06},
                {0.95, 0.28, 0.39, 0.66, 0.51, 0.4, 0.01}
        };

        double[][] Y1 = {{1, 0}, {0, 1}};
        double[][] Y2 = {{-1, 0}, {0, -1}};
        double[][][] kernels = {Y1, Y2};

        CNNPropagationData data = new CNNPropagationData(inputMatrix, kernels);
        CNNPropagationAlgorithm algorithm = new CNNPropagationAlgorithm("relu", "max");
        algorithm.initialize(data);
        String result = algorithm.execute();
        assertNotNull(result);
        assertTrue(result.contains("CNNPropagationAlgorithm"));
        assertTrue(result.contains("Выходные сигналы:"));
        assertTrue(result.contains("MSE:"));
    }

    @Test
    void invalidKernelsCount() {
        double[][] inputMatrix = {{1, 2}, {3, 4}};
        double[][][] kernels = {{{1}}};
        CNNPropagationData data = new CNNPropagationData(inputMatrix, kernels);
        CNNPropagationAlgorithm algorithm = new CNNPropagationAlgorithm("relu", "max");
        assertThrows(InvalidAlgorithmParameterException.class, () -> algorithm.initialize(data));
    }
}
