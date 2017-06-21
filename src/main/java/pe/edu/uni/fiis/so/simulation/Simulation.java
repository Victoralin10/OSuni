package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.events.SimulationActionListener;
import pe.edu.uni.fiis.so.simulation.events.SimulationEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vcueva on 6/21/17.
 */
public class Simulation {
    private static Simulation instance;

    public static Simulation getInstance() {
        if (instance == null) {
            instance = new Simulation();
        }

        return instance;
    }

    private Map <String, ArrayList <SimulationActionListener> > listeners;

    private String[] eventsName = new String[]{""};

    public Simulation() {
        listeners = new TreeMap<>();

        for (String anEventsName : eventsName) {
            listeners.put(anEventsName, new ArrayList<>());
        }
    }

    public synchronized void on(SimulationActionListener listener) {
        if (!listeners.containsKey(listener.getEvent())) {
            throw new RuntimeException("Event Key not found.");
        }

        listeners.get(listener.getEvent()).add(listener);
    }

    public synchronized void remove(SimulationActionListener listener) {
        if (!listeners.containsKey(listener.getEvent())) {
            throw new RuntimeException("Event Key not found.");
        }

        listeners.get(listener.getEvent()).remove(listener);
    }

    public synchronized void dispatchEvent(String eventCode, SimulationEvent event) {
        if (!listeners.containsKey(eventCode)) {
            throw new RuntimeException("Event Key not found.");
        }

        for (SimulationActionListener listener: listeners.get(eventCode)) {
            new Thread(() -> {
                try {
                    listener.actionPerformed(event);
                } catch (Exception | Error ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }
}
