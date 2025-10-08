package io.github.zevtos.dmathcore.algorithm.nn;

import java.security.InvalidAlgorithmParameterException;
import java.util.Arrays;

public class CNNPropagationAlgorithm {

    private final String activationFunction;
    private final String subsamplingFunction;
    private double[][] inputMatrix;
    private double[][][] kernels;
    private int n2;
    private int n3 = 2;

    public CNNPropagationAlgorithm(String activationFunction, String subsamplingFunction) {
        this.activationFunction = activationFunction;
        this.subsamplingFunction = subsamplingFunction;
    }

    public void initialize(Object input) throws InvalidAlgorithmParameterException {
        try {
            CNNPropagationData data = (CNNPropagationData) input;
            this.inputMatrix = data.getInputMatrix();
            this.kernels = data.getKernels();
            if (kernels.length != 2) throw new InvalidAlgorithmParameterException("kernels length must be 2");
            this.n2 = kernels[0].length;
        } catch (Exception e) {
            throw new InvalidAlgorithmParameterException(e.getMessage());
        }
    }

    public String execute() {
        StringBuilder return_text = new StringBuilder();
        return_text.append("```CNNPropagationAlgorithm\n");
        return_text.append("Введенная матрица данных:\n").append(matrixToString(inputMatrix)).append("\n");
        for (int i = 0; i < kernels.length; i++) {
            return_text.append("Ядро свёртки ").append(i + 1).append(":\n").append(matrixToString(kernels[i])).append("\n");
        }
        return_text.append("Функция активации: ").append(activationFunction).append("\n");
        return_text.append("Функция подвыборки: ").append(subsamplingFunction).append("\n");

        double[][][] convolutions = new double[6][][];
        for (int i = 0; i < 2; i++) {
            convolutions[i] = applyConvolution(inputMatrix, kernels[i]);
        }
        double[][][] poolings = new double[6][][];
        for (int i = 0; i < 2; i++) {
            poolings[i] = applyPooling(convolutions[i], n3);
        }

        for (int i = 2; i < 6; i++) {
            poolings[i] = applyPooling(applyConvolution(poolings[(i >= 4 ? 0 : 1)], kernels[i % 2]), n3);
        }

        double[][] finalOutput = buildFinalOutput(poolings);
        double mse = calculateMSE(finalOutput);

        StringBuilder result = new StringBuilder(return_text);
        result.append("Результат свёртки и подвыборки:\n");
        for (int i = 0; i < 2; i++) {
            result.append("Слой свёртки ").append(i + 1).append(":\n").append(matrixToString(convolutions[i]));
        }
        for (int i = 0; i < 2; i++) {
            result.append("Слой подвыборки ").append(i + 1).append(":\n").append(matrixToString(poolings[i]));
        }
        for (int i = 2; i < 6; i++) {
            result.append("Слой подвыборки ").append(i + 1).append(":\n").append(matrixToString(poolings[i]));
        }
        result.append("Выходные сигналы:\n").append(matrixToString(finalOutput));
        result.append("MSE: ").append(String.format("%.2f", mse)).append("\n");
        result.append("```");
        return result.toString();
    }

    private double[][] applyConvolution(double[][] input, double[][] kernel) {
        int n1 = input.length;
        int n1_prime = n1 - n2 + 1;
        double[][] output = new double[n1_prime][n1_prime];
        for (int i = 0; i < n1_prime; i++) {
            for (int j = 0; j < n1_prime; j++) {
                double sum = 0;
                for (int ki = 0; ki < n2; ki++) {
                    for (int kj = 0; kj < n2; kj++) {
                        sum += input[i + ki][j + kj] * kernel[ki][kj];
                    }
                }
                if (activationFunction.equalsIgnoreCase("relu")) {
                    sum = Math.max(0, sum);
                }
                output[i][j] = Math.round(sum * 100) / 100d;
            }
        }
        return output;
    }

    private double[][] applyPooling(double[][] input, int poolSize) {
        int n1_prime = input.length;
        int n1_double_prime = n1_prime / poolSize;
        double[][] output = new double[n1_double_prime][n1_double_prime];
        for (int i = 0; i < n1_double_prime; i++) {
            for (int j = 0; j < n1_double_prime; j++) {
                double poolValue;
                if (subsamplingFunction.equalsIgnoreCase("avg")) {
                    double sum = 0;
                    for (int pi = 0; pi < poolSize; pi++) {
                        for (int pj = 0; pj < poolSize; pj++) {
                            sum += input[i * poolSize + pi][j * poolSize + pj];
                        }
                    }
                    poolValue = sum / (poolSize * poolSize);
                } else {
                    double max = Double.NEGATIVE_INFINITY;
                    for (int pi = 0; pi < poolSize; pi++) {
                        for (int pj = 0; pj < poolSize; pj++) {
                            max = Math.max(max, input[i * poolSize + pi][j * poolSize + pj]);
                        }
                    }
                    poolValue = max;
                }
                output[i][j] = Math.round(poolValue * 100) / 100d;
            }
        }
        return output;
    }

    private double[][] buildFinalOutput(double[][][] inputs) {
        int n = 4;
        double[][] output = new double[1][n];
        for (int i = 0; i < n; i++) {
            output[0][i] = inputs[i + 2][0][0];
        }
        return output;
    }

    private double calculateMSE(double[][] output) {
        double sum = 0;
        for (int i = 0; i < output[0].length; i++) {
            sum += Math.pow(output[0][i] - (output[0][i] > 0.5 ? 1 : 0), 2);
        }
        return sum / output[0].length;
    }

    private String matrixToString(double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}
