package pe.edu.uni.fiis.so.simulation.process.interrupts;

/**
 * Created by vcueva on 7/10/17.
 */
public class SleepInterruption implements InterruptionInterface {

    private long startTime;
    private long durationTime;

    public SleepInterruption(long startTime, long durationTime) {
        this.startTime = startTime;
        this.durationTime = durationTime;
    }

    @Override
    public boolean isResolved(long timestamp) {
        return timestamp > startTime + durationTime;
    }
}
