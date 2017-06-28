package pe.edu.uni.fiis.so.simulation;


import pe.edu.uni.fiis.so.simulation.policies.PolicyManager;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by vcueva on 6/21/17.
 */
public class Kernel {
    public static final int STATE_INSTANCED = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_READY = 4;

    private final Machine machine;
    private ProcessManager processManager;
    private PolicyManager policyManager;

    private boolean powerOffSignal;
    private int state;

    // Locks
    private Lock kernelLock;
    private Lock processManagerLock;
    // end - Locks

    public Kernel(Machine machine) {
        this.machine = machine;
        this.powerOffSignal = false;
        this.state = STATE_INSTANCED;
        processManagerLock = new ReentrantLock();
        kernelLock = new ReentrantLock();
    }

    public void run(Cpu cpu) {
        kernelLock.lock();
        if (state == STATE_INSTANCED) {
            state = STATE_LOADING;
            processManager = new ProcessManager();
            policyManager = new PolicyManager(machine.getCpus());
        }
        kernelLock.unlock();

        while (true) {

        }
    }

    public void powerOffSignal() {
        powerOffSignal = true;
    }
}
