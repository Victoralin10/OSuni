package pe.edu.uni.fiis.so.util;

import java.util.Properties;

/**
 * Created by vcueva on 6/24/17.
 * <p>
 * This module loads the configuration of the simulation.
 * <p>
 * Contains methods for obtaining the values. If the key
 * does not exist the null value default should be returned.
 */
public class GlobalConfig {

    private static Properties properties = new Properties();

    public static void load(String file) {

    }

    public static void setProperty(String key, String value) {

    }

    public static String getString(String key, String defaultValue) {
        return defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        return defaultValue;
    }

    public static long getLong(String key, long defaultValue) {
        return defaultValue;
    }

    public static float getFloat(String key, float defaultValue) {
        return defaultValue;
    }

    public static double getDouble(String key, double defaultValue) {
        return defaultValue;
    }
}
