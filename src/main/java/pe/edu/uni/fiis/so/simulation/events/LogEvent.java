package pe.edu.uni.fiis.so.simulation.events;

/**
 * Created by vcueva on 7/12/17.
 */
public class LogEvent extends SimulationEvent {
    public LogEvent(String line) {
        super();
        putValue("line", line);
    }
}
