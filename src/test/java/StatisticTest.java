import org.junit.Assert;
import org.junit.Test;
import pe.edu.uni.fiis.so.statistic.StatisticModel;
import pe.edu.uni.fiis.so.statistic.StatisticParser;

/**
 * Created by vcueva on 6/21/17.
 */
public class StatisticTest {
    @Test
    public void testUniform() {
        StatisticModel sm = StatisticParser.parse("uniform(-20, 100)");
        Assert.assertNotNull("Parsing uniform(-20, 100)", sm);

        System.out.println("Testing: uniform(-20, 100)");

        for (int i = 0; i < 200; i++) {
            long v = sm.nextLongRandom();

            Assert.assertTrue("Value generated out of range:" + v, v >= -20 && v <= 100);
        }
    }

    @Test
    public void testExponential() {
        StatisticModel sm = StatisticParser.parse("exp(1.5)");
        Assert.assertNotNull("Parsing exp(1.5)", sm);

        System.out.println("Testing: exp(1.5)");

        for (int i = 0; i < 200; i++) {
            double v = sm.nexDoubleRandom();

            Assert.assertTrue("Value generated out of range: " + v, v >= 0);
        }
    }

    @Test
    public void testNormal() {
        StatisticModel sm = StatisticParser.parse("normal(50, 10)");
        Assert.assertNotNull("Parsing normal(50, 10)", sm);
    }

    @Test
    public void testList() {
        StatisticModel sm = StatisticParser.parse("int[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
        Assert.assertNotNull("Parsing int[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", sm);

        System.out.println("Testing: int[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");

        for (int i = 0; i < 20; i++) {
            long v = sm.nextLongRandom();

            Assert.assertTrue("Value not contains list: " + v, v > 0 && v <= 10);
        }
    }
}
