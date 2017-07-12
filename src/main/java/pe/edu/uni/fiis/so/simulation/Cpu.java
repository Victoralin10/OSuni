package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.events.CpuEvent;
import pe.edu.uni.fiis.so.util.Lib;

/**
 * Created by vcueva on 6/21/17.
 */
public class Cpu implements Runnable {

    public static final int INSTANTIATED = 1;
    public static final int RUNNING = 2;
    public static final int SLEEPING = 4;
    public static final int FINISHED = 8;

    private static int nextId = 0;

    private int id;
    private int state;
    private Thread thread;
    private Kernel kernel;

    private double avgTimeRunning;
    private long lastStatUpdateTime;
    private long lastStateChangeTime;
    private long runningTime;
    private long lastStatRunTime;

    public Cpu() {
        this(null);
    }

    public Cpu(Kernel kernel) {
        this.kernel = kernel;
        id = nextId++;
        state = INSTANTIATED;

        this.avgTimeRunning = 0;
        this.lastStatUpdateTime = 0;
        this.runningTime = 0;
        this.lastStateChangeTime = 0;
        this.lastStatRunTime = 0;
    }

    public int getId() {
        return id;
    }

    public void start() {
        if (state != INSTANTIATED) {
            throw new RuntimeException("Incorrect cpu state.");
        }

        thread = new Thread(this);
        thread.setName("" + id);
        thread.setDaemon(true);
        thread.start();

        setState(RUNNING);
    }

    public void updateStatistic() {
        long now = kernel.getMachine().getClock().getAbsoluteTime();
        if (now/1000 != this.lastStatUpdateTime/1000) {
            this.avgTimeRunning = (runningTime - lastStatRunTime)/10.0;
            Simulation.getInstance().dispatchEvent("cpu.updateStats", new CpuEvent(this));
            this.lastStatRunTime = runningTime;
        }
        this.lastStatUpdateTime = now;
    }

    public void setState(int state) {
        // updateStatistic();
        if (this.state != state) {
            long now = kernel.getMachine().getClock().getAbsoluteTime();
            if (this.state == RUNNING) {
                runningTime += now - lastStateChangeTime;
            }
            this.state = state;
            this.lastStateChangeTime = now;
            Simulation.getInstance().dispatchEvent("cpu.changeStatus", new CpuEvent(this));
        }
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public void run() {
        Lib.assertTrue(kernel != null);
        kernel.run(this);
        setState(FINISHED);
    }

    public double getAvgTimeRunning() {
        return avgTimeRunning;
    }

    public int getState() {
        return state;
    }
}
