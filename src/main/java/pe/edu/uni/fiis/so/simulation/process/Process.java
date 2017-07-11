package pe.edu.uni.fiis.so.simulation.process;

import pe.edu.uni.fiis.so.simulation.process.interrupts.InterruptionInterface;

/**
 * Created by vcueva on 6/28/17.
 */
public class Process {

    private Code code;

    private boolean interrupted;
    private InterruptionInterface interruption;
    private boolean errored;
    private boolean finished;

    public Process() {
        interrupted = false;
        errored = false;
        finished = false;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public InterruptionInterface getInterruption() {
        return interruption;
    }

    public void setInterruption(InterruptionInterface interruption) {
        this.interruption = interruption;
    }

    public boolean isErrored() {
        return errored;
    }

    public void setErrored(boolean errored) {
        this.errored = errored;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
