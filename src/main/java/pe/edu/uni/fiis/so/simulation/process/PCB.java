package pe.edu.uni.fiis.so.simulation.process;

import pe.edu.uni.fiis.so.simulation.Kernel;
import pe.edu.uni.fiis.so.simulation.Simulation;
import pe.edu.uni.fiis.so.simulation.events.ProcessEvent;

/**
 * Created by vcueva on 6/28/17.
 */

public class PCB implements Comparable {
    /**
     * State code for a process.
     */
    public static final int NEW = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int WAITING = 3;
    public static final int FINISHED = 4;

    /**
     * Static variable that store the next id for a process.
     */
    private static int nextPid = 1;

    /**
     * Id of the process.
     */
    private int pid;

    /**
     * Processor Number where is executing the process if the status is RUNNING,
     * else the last processor where was executed.
     */
    private int cpuNumber;

    /**
     * Status of the process.
     */
    private int processStatus;

    /**
     * Process about
     */
    private Process process;

    /**
     * Priority of the process.
     */
    private int priority;

    private String name = "";

    /**
     * Program Counter.
     */
    private ProgramCounter programCounter;

    // STATISTICS
    private long lastChangeStatusTimestamp;
    private long totalRunningTime;
    private long runningCount = 0;
    private double avgRunningTime = 0;
    private long minRunningTime = (1 << 20);
    private long maxRunningTime = 0;

    private long totalWaitingTime = 0;
    private long waitingCount = 0;
    private double avgWaitingTime = 0;
    private long minWaitingTime = (1 << 20);
    private long maxWaitingTime = 0;

    private long totalReadyTime = 0;
    private long readyCount = 0;
    private double avgReadyTime = 0;
    private long minReadyTime = (1 << 20);
    private long maxReadyTime = 0;
    // END STATISTICS

    public PCB(String name) {
        this();
        this.name = name;
    }

    public PCB() {
        this.cpuNumber = -1;
        this.processStatus = NEW;
        this.priority = 0;  //
        this.pid = nextPid;
        this.totalRunningTime = 0;
        nextPid++;

        this.programCounter = new ProgramCounter();
        lastChangeStatusTimestamp = Kernel.instance.getMachine().getClock().getAbsoluteTime();
    }

    public int getPid() {
        return pid;
    }

    public int getCpuNumber() {
        if (processStatus != PCB.RUNNING) {
            return -1;
        }
        return cpuNumber;
    }

    public void setCpuNumber(int cpuNumber) {
        this.cpuNumber = cpuNumber;
    }

    public int getLastProcessorNumber() {
        return cpuNumber;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        if (this.processStatus == processStatus) {
            return;
        }
        long ct = Kernel.instance.getMachine().getClock().getAbsoluteTime();
        long delta = ct - lastChangeStatusTimestamp;
        if (this.processStatus == RUNNING) {
            totalRunningTime += delta;
            runningCount++;
            avgRunningTime = totalRunningTime / ((double) runningCount);
            minRunningTime = Math.min(minRunningTime, delta);
            maxRunningTime = Math.max(maxRunningTime, delta);
        } else if (this.processStatus == WAITING) {
            totalWaitingTime += delta;
            waitingCount++;
            avgWaitingTime = totalWaitingTime / ((double) waitingCount);
            minWaitingTime = Math.min(minWaitingTime, delta);
            maxWaitingTime = Math.max(maxWaitingTime, delta);
        } else if (this.processStatus == READY) {
            totalReadyTime += delta;
            readyCount++;
            avgReadyTime = totalReadyTime / ((double) readyCount);
            minReadyTime = Math.min(minReadyTime, delta);
            maxReadyTime = Math.max(maxReadyTime, delta);
        }

        this.processStatus = processStatus;
        Simulation.getInstance().dispatchEvent("process.changeStatus", new ProcessEvent(this));
        lastChangeStatusTimestamp = ct;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority < -20 || priority > 19) {
            System.err.println("Invalid priority on process with pid " + this.getPid());
            return;
        }
        if (this.priority == priority) {
            return;
        }

        this.priority = priority;
    }

    public long getTotalRunningTime() {
        return totalRunningTime;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getName() {
        return name;
    }

    public ProgramCounter getProgramCounter() {
        return programCounter;
    }

    @Override
    public int compareTo(Object o) {
        PCB os = (PCB) o;
        return Integer.compare(getPid(), os.getPid());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != getClass()) {
            return false;
        }

        PCB p2 = (PCB) o;
        return getPid() == p2.getPid();
    }

    public double getAvgRunningTime() {
        return avgRunningTime;
    }

    public long getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public double getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public long getTotalReadyTime() {
        return totalReadyTime;
    }

    public double getAvgReadyTime() {
        return avgReadyTime;
    }

    public long getMinRunningTime() {
        return minRunningTime;
    }

    public long getMaxRunningTime() {
        return maxRunningTime;
    }

    public long getMinWaitingTime() {
        return minWaitingTime;
    }

    public long getMaxWaitingTime() {
        return maxWaitingTime;
    }

    public long getMinReadyTime() {
        return minReadyTime;
    }

    public long getMaxReadyTime() {
        return maxReadyTime;
    }
}

