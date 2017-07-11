package pe.edu.uni.fiis.so.simulation.process;

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
     * The time of the process running.
     */
    private long runningTime;

    /**
     * Program Counter.
     */
    private ProgramCounter programCounter;

    public PCB(String name) {
        this();
        this.name = name;
    }

    public PCB() {
        this.cpuNumber = -1;
        this.processStatus = NEW;
        this.priority = 0;  //
        this.pid = nextPid;
        this.runningTime = 0;
        nextPid++;

        this.programCounter = new ProgramCounter();
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

        this.processStatus = processStatus;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority < -20 || priority > 19) {
            System.err.println("Invalid priority on process with pid " + this.getPid());
            return;
        }
        this.priority = priority;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
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
}

