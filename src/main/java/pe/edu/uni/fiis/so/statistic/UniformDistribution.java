package pe.edu.uni.fiis.so.statistic;

/**
 * Created by Luis Angel on 21/06/2017.
 */
public class UniformDistribution implements StatisticModel{

    private int a, b;

    public UniformDistribution(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public long nextLongRandom() {
        return (long)this.nexDoubleRandom();
    }

    @Override
    public double nexDoubleRandom() {
        double r;
        double num = 0;
        try {
            if(a<b){
                r = Math.random();
                num = a + r * (b - a);
            }
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
