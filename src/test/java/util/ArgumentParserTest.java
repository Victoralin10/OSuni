package util;

/**
 * Created by vcueva on 6/21/17.
 */

import org.junit.Assert;
import org.junit.Test;
import pe.edu.uni.fiis.so.util.ArgumentParser;

import java.util.List;

public class ArgumentParserTest {
    @Test
    public void testImplementation() {
        Assert.assertNotNull("Method not implemented.", ArgumentParser.parse("run"));
    }

    @Test
    public void testWorksSimple() {
        List<String> result = ArgumentParser.parse(" run hello   world  ");

        Assert.assertEquals("Bad number of arguments.", 3, result.size());

        String[] expected = new String[]{"run", "hello", "world"};

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testWorksQuotes() {
        List<String> result = ArgumentParser.parse(" run \"hello   world\"  ");

        Assert.assertEquals("Bad number of arguments.", 2, result.size());

        String[] expected = new String[]{"run", "hello   world"};

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testWorkEscape() {
        List<String> result = ArgumentParser.parse(" run \"hel\\\"lo   world\"  ");

        Assert.assertEquals("Bad number of arguments.", 2, result.size());

        String[] expected = new String[]{"run", "hel\"lo   world"};

        Assert.assertArrayEquals(expected, result.toArray());
    }
}
