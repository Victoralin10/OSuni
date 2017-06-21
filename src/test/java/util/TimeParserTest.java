package util;

import org.junit.Assert;
import org.junit.Test;
import pe.edu.uni.fiis.so.util.SizeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vcueva on 6/21/17.
 */
public class TimeParserTest {
    private String[] sizes = new String[]{"ms", "s", "m", "h", "d"};
    private int[] numbers = new int[]{3, 17, 29, 100};

    @Test
    public void testSimple() {
        for (int i = 0; i < sizes.length; i++) {
            for (int number : numbers) {
                long expected = (long) (number * Math.pow(10, 3*i) + 0.1);
                List<String> args = new ArrayList<>();
                args.add(number + sizes[i]);

                long result = SizeParser.parse(args);
                Assert.assertEquals("Input: " + args.toString(), expected, result);
            }
        }
    }

    @Test
    public void testCompose() {
        List<String> args = new ArrayList<>();
        long expected = 0;

        args.add(numbers[0] + sizes[1]);
        expected += numbers[0]*(1000);

        args.add(numbers[2] + sizes[3]);
        expected += numbers[2]*(60L*60*1000);

        Assert.assertEquals("Input: " + args.toString(),expected, SizeParser.parse(args));
    }
}
