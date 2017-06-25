package util;

import org.junit.Assert;
import org.junit.Test;
import pe.edu.uni.fiis.so.util.SizeParser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by vcueva on 6/21/17.
 */
public class SizeParserTest {
    private String[] sizes = new String[]{"b", "kb", "mb", "gb", "tb"};
    private int[] numbers = new int[]{3, 17, 29, 100};

    @Test
    public void testSimple() {
        for (int i = 0; i < sizes.length; i++) {
            for (int number : numbers) {
                String input = number + sizes[i];
                long expected = number * (1L << (10 * i));

                long result = SizeParser.parse(input);
                Assert.assertEquals("Input: " + input, expected, result);

                input = number + sizes[i].toUpperCase();
                Assert.assertEquals("Input: " + input, expected, result);
            }
        }
    }

    @Test
    public void testCompose() {
        List<String> args = new ArrayList<>();
        long expected = 0;

        args.add(numbers[0] + sizes[1]);
        expected += numbers[0] * (1L << 10);

        args.add(numbers[2] + sizes[3]);
        expected += numbers[2] * (1L << 30);

        Assert.assertEquals("Input: " + args.toString(), expected, SizeParser.parse(args));
    }

    @Test
    public void testToString() {
        long[] input = new long[]{117, 15<<10, (7<<20) + 17, (15L<<30) + (12<<10), 7L<<40};
        String[] expectedOut = new String[]{"117b", "15Kb", "7Mb 17b", "15Gb 12Kb", "7Tb"};

        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals("Input: " + input[i], expectedOut[i], SizeParser.toString(input[i]));
        }
    }
}
