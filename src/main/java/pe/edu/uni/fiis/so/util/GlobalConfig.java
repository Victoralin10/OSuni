package pe.edu.uni.fiis.so.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
    private static String file = null;

    public static void load(String file) {
        try {
            properties.load(new FileReader(file));
            GlobalConfig.file = file;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public static String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return Integer.parseInt(properties.getProperty(key).trim());
    }

    public static long getLong(String key, long defaultValue) {
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return Long.parseLong(properties.getProperty(key).trim());
    }

    public static float getFloat(String key, float defaultValue) {
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return Float.parseFloat(properties.getProperty(key).trim());
    }

    public static double getDouble(String key, double defaultValue) {
        if (!properties.containsKey(key)) {
            return defaultValue;
        }
        return Double.parseDouble(properties.getProperty(key).trim());
    }

    public static void save() {
        if (file == null) return;

        try {
            properties.store(new FileOutputStream(new File(file)), "Global Config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
