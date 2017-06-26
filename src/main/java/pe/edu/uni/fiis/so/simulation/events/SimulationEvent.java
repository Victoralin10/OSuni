package pe.edu.uni.fiis.so.simulation.events;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vcueva on 6/21/17.
 */

public class SimulationEvent {

    private Map<String, Object> values;

    public SimulationEvent() {
        values = new TreeMap<>();
    }

    void putValue(String key, Object value) {
        values.put(key, value);
    }

    public String getString(String key) {
        return (String) values.get(key);
    }

    public Integer getInteger(String key) {
        return (Integer) values.get(key);
    }

    public Long getLong(String key) {
        return (Long) values.get(key);
    }

    public Float getFloat(String key) {
        return (Float) values.get(key);
    }

    public Double getDouble(String key) {
        return (Double) values.get(key);
    }
}
