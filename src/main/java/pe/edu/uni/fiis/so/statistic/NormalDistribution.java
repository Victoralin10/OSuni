package pe.edu.uni.fiis.so.statistic;


/**
 * Created by Luis Angel on 21/06/2017.
 */
public class NormalDistribution implements StatisticModel {

    private double mu, sigma;

    public NormalDistribution(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    @Override
    public long nextLongRandom() {
        return (long) this.nexDoubleRandom();
    }

    @Override
    public double nexDoubleRandom() {
        double num = 0;
        double sum = 0;

        try {
            for (int i = 0; i < 12; i++) {
                sum += Math.random();
            }

            num = mu + sigma * (sum - 6);
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
