package pe.edu.uni.fiis.so.simulation.process.interrupts;

/**
 * Created by vcueva on 7/11/17.
 */
public class DiscInterruption implements InterruptionInterface {

    private int interruptionResult;
    private boolean resolved;

    @Override
    public boolean isResolved(long timestamp) {
        return resolved;
    }

    public void setInterruptionResult(int interruptionResult) {
        this.interruptionResult = interruptionResult;
    }

    public void markAsResolved() {
        resolved = true;
    }

    @Override
    public int result() {
        return interruptionResult;
    }
}
