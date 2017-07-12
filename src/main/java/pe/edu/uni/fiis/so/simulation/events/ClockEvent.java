package pe.edu.uni.fiis.so.simulation.events;

/**
 * Created by vcueva on 7/12/17.
 */
public class ClockEvent extends SimulationEvent {
    public ClockEvent(long time) {
        super();
        putValue("time", time);
    }
}
