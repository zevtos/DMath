package io.github.zevtos.dmathcore.algorithm.optimization;

public class PotentialMethodData {
    private final double[][] costMatrix;
    private final double[] supply;
    private final double[] demand;

    public PotentialMethodData(double[][] costMatrix, double[] supply, double[] demand) {
        this.costMatrix = costMatrix;
        this.supply = supply;
        this.demand = demand;
    }

    public double[][] getCostMatrix() {
        return costMatrix;
    }

    public double[] getSupply() {
        return supply;
    }

    public double[] getDemand() {
        return demand;
    }
}
