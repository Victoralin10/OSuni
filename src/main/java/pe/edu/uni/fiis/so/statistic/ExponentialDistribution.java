package pe.edu.uni.fiis.so.statistic;

/**
 * Created by Luis Angel on 21/06/2017.
 */
public class ExponentialDistribution implements StatisticModel {
    private double lambdA;

    public ExponentialDistribution(double lambda) {
        this.lambdA = lambda;
    }

    @Override
    public long nextLongRandom() {
        return (long) this.nexDoubleRandom();
    }

    @Override
    public double nexDoubleRandom() {
        double num = 0;
        double r = Math.random();

        try {
            num = -((1 / lambdA) * Math.log(1 - r));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return num;
    }

    @Override
    public String nextString() {
        return this.nexDoubleRandom() + "";
    }
}
