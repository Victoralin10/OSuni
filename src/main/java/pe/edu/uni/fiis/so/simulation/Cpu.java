package pe.edu.uni.fiis.so.simulation;

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

    public Cpu() {
        this(null);
    }

    public Cpu(Kernel kernel) {
        this.kernel = kernel;
        id = nextId++;
        state = INSTANTIATED;
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

    public void setState(int state) {
        if (this.state != state) {
            this.state = state;
            // Dispatch event
        }
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public void run() {
        Lib.assertTrue(kernel != null);
        kernel.run(this);
        state = FINISHED;
    }
}
