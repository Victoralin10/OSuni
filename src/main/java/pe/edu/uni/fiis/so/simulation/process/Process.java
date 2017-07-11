package pe.edu.uni.fiis.so.simulation.process;

import pe.edu.uni.fiis.so.simulation.process.interrupts.InterruptionInterface;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vcueva on 6/28/17.
 */
public class Process {

    private Code code;

    private boolean interrupted;
    private InterruptionInterface interruption;
    private boolean errored;
    private boolean finished;

    private String name;
    private long size;

    private Map<String, Object> memory;

    public Process() {
        interrupted = false;
        errored = false;
        finished = false;

        memory = new TreeMap<>();
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
        this.name = code.getName();
        this.size = code.getSize();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Map<String, Object> getMemory() {
        return memory;
    }
}
