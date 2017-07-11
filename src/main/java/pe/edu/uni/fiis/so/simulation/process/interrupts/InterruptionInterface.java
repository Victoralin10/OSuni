package pe.edu.uni.fiis.so.simulation.process.interrupts;

/**
 * Created by vcueva on 7/10/17.
 */

public interface InterruptionInterface {
    boolean isResolved(long timestamp);
    int result();
}
