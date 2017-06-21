package pe.edu.uni.fiis.so.simulation.events;

/**
 * Created by vcueva on 6/21/17.
 */
public abstract class SimulationActionListener {
    private static long nextId = 0;

    private long id;

    private String event;

    public SimulationActionListener(String event) {
        id = nextId++;

        this.event = event;
    }

    private long getId() {
        return id;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) return false;

        SimulationActionListener x = (SimulationActionListener) o;
        return id == x.getId();
    }

    public abstract void actionPerformed(SimulationEvent event);
}
