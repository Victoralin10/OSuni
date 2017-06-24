package pe.edu.uni.fiis.so.statistic;


/**
 * Created by Luis Angel on 21/06/2017.
 */
public class OriginalDistribution implements StatisticModel {

    private String numbers;

    public OriginalDistribution(String numbers) {
        this.numbers = numbers;
    }

    public static String[] parseLine(String str) {
        String[] ListNum = null;

        String type = str.substring(0, 3);


        if (type.equals("int") || type.equals("str")) {

            str = str.substring(4);
            str = str.replace(" ", "");
            str = str.substring(0, str.indexOf("]"));

            ListNum = str.split(",");

        } else {
            System.out.println("Cadena no valida");
        }
        return ListNum;

    }

    @Override
    public long nextLongRandom() {
        String[] num = parseLine(numbers);
        double r = Math.random();
        int pos = (int) (r * (num.length));
        return Long.parseLong(num[pos]);
    }

    @Override
    public double nexDoubleRandom() {
        return 0;
    }

    @Override
    public String nextString() {
        return this.nextLongRandom() + "";
    }
}

