package io.github.zevtos.dmathcore.algorithm.nn;

public class CNNPropagationData {
    private final double[][] inputMatrix;
    private final double[][][] kernels;

    public CNNPropagationData(double[][] inputMatrix, double[][][] kernels) {
        this.inputMatrix = inputMatrix;
        this.kernels = kernels;
    }

    public double[][] getInputMatrix() {
        return inputMatrix;
    }

    public double[][][] getKernels() {
        return kernels;
    }
}
