package util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pe.edu.uni.fiis.so.util.GlobalConfig;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by vcueva on 6/25/17.
 */
public class GlobalConfigTest {
    @Test
    public void testSave() {
        GlobalConfig.setProperty("myKey", "value1");

        Assert.assertEquals("value1", GlobalConfig.getString("myKey", ""));

        GlobalConfig.setProperty("myKey", "100");
        Assert.assertEquals(100, GlobalConfig.getInt("myKey", 100));
        Assert.assertEquals(100L, GlobalConfig.getLong("myKey", 10L));

        GlobalConfig.setProperty("myKey", "200.15");
        Assert.assertEquals(200.15f, GlobalConfig.getFloat("myKey", 1.0f), 1e-10);
        Assert.assertEquals(200.15, GlobalConfig.getDouble("myKey", 1.0), 1e-10);
    }

    @Test
    public void testDefault() {
        Assert.assertThat(GlobalConfig.getString("xd", "xd"), is("xd"));
        Assert.assertThat(GlobalConfig.getInt("xd", 10), is(10));
        Assert.assertThat(GlobalConfig.getLong("xd", 10L), is(10L));
        Assert.assertThat(GlobalConfig.getFloat("xd", 10.5f), is(10.5f));
        Assert.assertThat(GlobalConfig.getDouble("xd", 15.005), is(15.005));
    }

    @Test
    public void testLoadFromFile() {
        String x = getClass().getClassLoader().getResource("globalconfig.conf").getPath();

        GlobalConfig.load(x);

        Assert.assertThat(GlobalConfig.getString("testKey", ""), is("testValue"));
        Assert.assertThat(GlobalConfig.getString("testKey2", ""), is("testValue2"));
    }
}
