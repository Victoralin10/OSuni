package util;

import org.junit.*;
import pe.edu.uni.fiis.so.util.TimeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vcueva on 6/21/17.
 */
public class TimeParserTest {
    private String[] sizes = new String[]{"ms", "s", "m", "h", "d"};
    private int[] numbers = new int[]{3, 17, 29, 100};

    @Before
    public void testSimple() {
        for (int i = 0; i < sizes.length; i++) {
            for (int number : numbers) {
                long expected = (long) (number * Math.pow(10, 3 * i) + 0.1);
                long result = TimeParser.parse(number + sizes[i]);
                Assert.assertEquals("Input: " + number + sizes[i], expected, result);
            }
        }
    }

    @Test
    public void testCompose() {
        List<String> args = new ArrayList<>();
        long expected = 0;

        args.add(numbers[0] + sizes[1]);
        expected += numbers[0] * (1000);

        args.add(numbers[2] + sizes[3]);
        expected += numbers[2] * (60L * 60 * 1000);

        Assert.assertEquals("Input: " + args.toString(), expected, TimeParser.parse(args));
    }

    @After
    public void testToString() {
        long[] input = new long[]{117, 1017, 2*60*100 + 29*100 + 15, 17*60*60*100 + 47*100};
        String[] expectedOut = new String[]{"117ms", "1s 17ms", "2m 29s 15ms", "17h 47s"};

        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals("Input: " + input[i], expectedOut[i], TimeParser.toString(input[i]));
        }
    }
}
