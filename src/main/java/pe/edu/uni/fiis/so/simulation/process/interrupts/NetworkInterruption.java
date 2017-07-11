package pe.edu.uni.fiis.so.simulation.process.interrupts;

/**
 * Created by vcueva on 7/11/17.
 */
public class NetworkInterruption implements InterruptionInterface {
    private int interruptionResult;
    private boolean resolved;

    public void setInterruptionResult(int interruptionResult) {
        this.interruptionResult = interruptionResult;
    }

    public void markAsResolved() {
        resolved = true;
    }

    @Override
    public boolean isResolved(long timestamp) {
        return resolved;
    }

    @Override
    public int result() {
        return interruptionResult;
    }
}
