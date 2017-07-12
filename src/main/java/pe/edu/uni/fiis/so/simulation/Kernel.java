package pe.edu.uni.fiis.so.simulation;


import pe.edu.uni.fiis.so.simulation.memory.FirstFitManager;
import pe.edu.uni.fiis.so.simulation.memory.MemoryManagerInterface;
import pe.edu.uni.fiis.so.simulation.policies.PolicyManager;
import pe.edu.uni.fiis.so.simulation.process.*;
import pe.edu.uni.fiis.so.simulation.process.Process;
import pe.edu.uni.fiis.so.simulation.services.DiscServiceRequest;
import pe.edu.uni.fiis.so.simulation.services.MemoryServiceRequest;
import pe.edu.uni.fiis.so.simulation.services.NetworkServiceRequest;
import pe.edu.uni.fiis.so.simulation.services.StartupServiceRequest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by vcueva on 6/21/17.
 */
public class Kernel {
    public static final int STATE_INSTANCED = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_READY = 4;

    public static Kernel instance;

    private final Machine machine;
    private ProcessManager processManager;
    private PolicyManager policyManager;
    private Syscall syscall;
    private MemoryManagerInterface memoryManager;

    private boolean powerOffSignal;
    private int state;

    // Locks
    private Lock kernelLock;
    private Lock processManagerLock;
    private Lock memoryLock;
    // end - Locks

    // Service queues
    private Queue<StartupServiceRequest> startupServiceQueue;
    private Queue<MemoryServiceRequest> memoryServiceQueue;
    private Queue<DiscServiceRequest> discServiceQueue;
    private Queue<NetworkServiceRequest> networkServiceQueue;
    // End Service queues

    public Kernel(Machine machine) {
        this.machine = machine;
        this.powerOffSignal = false;
        this.state = STATE_INSTANCED;
        processManagerLock = new ReentrantLock();
        kernelLock = new ReentrantLock();
        memoryLock = new ReentrantLock();
        instance = this;

        startupServiceQueue = new ConcurrentLinkedQueue<>();
        memoryServiceQueue = new ConcurrentLinkedQueue<>();
        discServiceQueue = new ConcurrentLinkedQueue<>();
        networkServiceQueue = new ConcurrentLinkedQueue<>();
    }

    public void setState(int state) {
        this.state = state;
    }

    ArrayList<String> getLinesFromFile(String name) {
        URL resource = getClass().getClassLoader().getResource(name);
        if (resource == null) {
            return null;
        }
        ArrayList<String> ans = new ArrayList<>();
        try {
            Scanner in = new Scanner(resource.openStream());
            while (in.hasNextLine()) {
                ans.add(in.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }

    private void runSystemd() {
        ArrayList<String> xd = getLinesFromFile("bin/systemd.so");

        PCB pcb = new PCB();
        pcb.setPriority(-20);
        processManager.addProcess(pcb);

        Process process = new Process();
        pcb.setProcess(process);

        Code mc = new Code();
        mc.load(xd);
        process.setCode(mc);
        memoryManager.malloc(process.getSize(), pcb.getPid());
        processManager.toReady(pcb);
    }

    public void run(Cpu cpu) {
        kernelLock.lock();
        if (state == STATE_INSTANCED) {
            setState(STATE_LOADING);
            Simulation.getInstance().log("Cargando kernel en cpu" + cpu.getId());
            memoryManager = new FirstFitManager(machine.getMemory());
            processManager = new ProcessManager();
            policyManager = new PolicyManager(machine.getCpus());
            syscall = new Syscall(this);
            runSystemd();
            setState(STATE_READY);
        }
        kernelLock.unlock();

        Simulation.getInstance().log("cpu" + cpu.getId() + " ejecutando kernel.");
        while (!powerOffSignal) {
            cpu.updateStatistic();

            Simulation.getInstance().log("cpu" + cpu.getId() + ": seleccionando proceso listo para ejecutar.");
            PCB pcb = null;
            processManagerLock.lock();
            ArrayList<PCB> readys = new ArrayList<>();
            long ct = machine.getClock().getAbsoluteTime();
            for (PCB pcb1 : processManager.getBlockedQueue()) {
                if (pcb1.getProcess().getInterruption().isResolved(ct)) {
                    readys.add(pcb1);
                }
            }
            for (PCB pcb1 : readys) {
                processManager.toReady(pcb1);
                pcb1.getProcess().setInterrupted(false);
                // pcb1.getProgramCounter().advance();
            }
            if (processManager.getReadyQueue().size() > 0) {
                pcb = policyManager.getPolicy(cpu).next(processManager.getReadyQueue());
                pcb.setCpuNumber(cpu.getId());
                processManager.toRunning(pcb);
            }
            processManagerLock.unlock();

            if (pcb != null) {
                Simulation.getInstance().log("cpu" + cpu.getId() + ": ejecutando pid " + pcb.getPid());
                ProgramCounter pc = pcb.getProgramCounter();
                Process p = pcb.getProcess();
                Sentence sentence = p.getCode().getSentenceByAddress(pc.getCurrentAddress());
                if (sentence.isLabel()) {
                    pc.advance();
                    sentence = p.getCode().getSentenceByAddress(pc.getCurrentAddress());
                }
                try {
                    Class[] argTypes = new Class[]{Cpu.class, PCB.class, sentence.getArguments().getClass(), Integer.class};
                    Method m = syscall.getClass().getDeclaredMethod(sentence.getFunction(), argTypes);
                    int t = (int) m.invoke(syscall, cpu, pcb, sentence.getArguments(), 200);
                    sleep(t);
                } catch (NoSuchMethodException e) {
                    p.setErrored(true);
                    e.printStackTrace();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                pcb.setCpuNumber(-1);

                processManagerLock.lock();
                if (p.isFinished()) {
                    Simulation.getInstance().log("cpu" + cpu.getId() + ": finalizando pid " + pcb.getPid());
                    processManager.removeProcess(pcb);
                    memoryLock.lock();
                    memoryManager.free(pcb.getPid());
                    memoryLock.unlock();
                } else if (p.isErrored()) {
                    Simulation.getInstance().log("cpu" + cpu.getId() + ": error pid " + pcb.getPid());
                    processManager.removeProcess(pcb);
                    memoryLock.lock();
                    memoryManager.free(pcb.getPid());
                    memoryLock.unlock();
                } else if (!p.isInterrupted()) {
                    Simulation.getInstance().log("cpu" + cpu.getId() + ": tiempo en cpu agotado " + pcb.getPid());
                    pc.advance();
                    processManager.toReady(pcb);
                } else {
                    Simulation.getInstance().log("cpu" + cpu.getId() + ": Proceso con interrupcion " + pcb.getPid());
                    processManager.toWaiting(pcb);
                }
                processManagerLock.unlock();
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

    public Machine getMachine() {
        return machine;
    }

    ProcessManager getProcessManager() {
        return processManager;
    }

    Lock getProcessManagerLock() {
        return processManagerLock;
    }

    MemoryManagerInterface getMemoryManager() {
        return memoryManager;
    }

    public Queue<StartupServiceRequest> getStartupServiceQueue() {
        return startupServiceQueue;
    }

    public Queue<MemoryServiceRequest> getMemoryServiceQueue() {
        return memoryServiceQueue;
    }

    public Lock getMemoryLock() {
        return memoryLock;
    }

    public Queue<DiscServiceRequest> getDiscServiceQueue() {
        return discServiceQueue;
    }

    public Queue<NetworkServiceRequest> getNetworkServiceQueue() {
        return networkServiceQueue;
    }
}
