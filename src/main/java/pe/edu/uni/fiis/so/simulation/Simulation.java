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
    public static final int SIMULATION_BEGIN_STATE = 1;
    public static final int SIMULATION_RUNNING_STATE = 2;
    public static final int SIMULATION_END_STATE = 4;

    private static Simulation instance;
    private Map<String, ArrayList<SimulationActionListener>> listeners;
    private String[] eventsName = new String[] {
            "process.changeStatus"
    };
    private int status;
    private Machine machine;

    public Simulation() {
        listeners = new TreeMap<>();

        for (String anEventsName : eventsName) {
            listeners.put(anEventsName, new ArrayList<>());
        }

        status = SIMULATION_BEGIN_STATE;
    }

    public static Simulation getInstance() {
        if (instance == null) {
            instance = new Simulation();
        }

        return instance;
    }

    public void start() {
        if (status == SIMULATION_RUNNING_STATE) {
            throw new RuntimeException("Incorrect simulation state.");
        }

        machine = new Machine();
        machine.powerOn();
        status = SIMULATION_RUNNING_STATE;
    }

    public void stop() {
        machine.powerOff();
        status = SIMULATION_END_STATE;
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

        for (SimulationActionListener listener : listeners.get(eventCode)) {
            Thread t = new Thread(() -> {
                try {
                    listener.actionPerformed(event);
                } catch (Exception | Error ex) {
                    ex.printStackTrace();
                }
            });
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
        }
    }
}
