package pe.edu.uni.fiis.so.simulation;


import pe.edu.uni.fiis.so.simulation.policies.PolicyManager;
import pe.edu.uni.fiis.so.simulation.process.PCB;
import pe.edu.uni.fiis.so.simulation.process.Process;
import pe.edu.uni.fiis.so.simulation.process.ProgramCounter;
import pe.edu.uni.fiis.so.simulation.process.Sentence;

import java.lang.reflect.Method;
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
    private Syscall syscall;

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

    public void setState(int state) {
        this.state = state;
    }

    public void run(Cpu cpu) {
        kernelLock.lock();
        if (state == STATE_INSTANCED) {
            setState(STATE_LOADING);
            processManager = new ProcessManager();
            policyManager = new PolicyManager(machine.getCpus());
            syscall = new Syscall(this);
            setState(STATE_READY);
        }
        kernelLock.unlock();

        while (!powerOffSignal) {
            PCB pcb = null;
            processManagerLock.lock();
            if (processManager.getReadyQueue().size() > 0) {
                pcb = policyManager.getPolicy(cpu).next(processManager.getReadyQueue());
                processManager.toRunning(pcb);
            }
            processManagerLock.unlock();

            if (pcb != null) {
                ProgramCounter pc = pcb.getProgramCounter();
                Process p = pcb.getProcess();
                Sentence sentence = p.getCode().getSentenceByAddress(pc.getCurrentAddress());
                if (sentence.isLabel()) {
                    pc.setNextAddress(pc.getCurrentAddress() + 1);
                    pc.setCurrentAddress(pc.getNextAddress());
                }
                try {
                    Method m = Syscall.class.getDeclaredMethod("");
                    // m.invoke(cpu, "hola");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } else {
                cpu.setState(Cpu.SLEEPING);
                sleep(10);
                cpu.setState(Cpu.RUNNING);
            }
        }
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration * machine.getClock().getFactor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void powerOffSignal() {
        powerOffSignal = true;
    }
}
