package pe.edu.uni.fiis.so.simulation;


import pe.edu.uni.fiis.so.simulation.memory.FirstFitManager;
import pe.edu.uni.fiis.so.simulation.memory.MemoryManagerInterface;
import pe.edu.uni.fiis.so.simulation.policies.PolicyManager;
import pe.edu.uni.fiis.so.simulation.process.*;
import pe.edu.uni.fiis.so.simulation.process.Process;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
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
    private MemoryManagerInterface memoryManager;

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

        memoryManager = new FirstFitManager(machine.getMemory());
    }

    public void setState(int state) {
        this.state = state;
    }

    private String getTextFromFile(String name) {
        URL resource = getClass().getClassLoader().getResource("bin/" + name);
        if (resource == null) {
            return null;
        }
        StringBuilder ans = new StringBuilder();
        try {
            Scanner in = new Scanner(resource.openStream());
            while (in.hasNextLine()) {
                ans.append(in.nextLine() + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans.toString();
    }

    private ArrayList<String> getLinesFromFile(String name) {
        URL resource = getClass().getClassLoader().getResource("bin/" + name);
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
        ArrayList<String> xd = getLinesFromFile("systemd.so");

        PCB pcb = new PCB();
        processManager.addProcess(pcb);

        Process process = new Process();
        pcb.setProcess(process);

        Code mc = new Code();
        mc.load(xd);
        process.setCode(mc);
        processManager.toReady(pcb);
    }

    public void run(Cpu cpu) {
        kernelLock.lock();
        if (state == STATE_INSTANCED) {
            setState(STATE_LOADING);
            processManager = new ProcessManager();
            policyManager = new PolicyManager(machine.getCpus());
            syscall = new Syscall(this);
            runSystemd();
            setState(STATE_READY);
        }
        kernelLock.unlock();

        while (!powerOffSignal) {
            PCB pcb = null;
            processManagerLock.lock();
            ArrayList<PCB> readys = new ArrayList<>();
            long ct = machine.getClock().getAbsoluteTime();
            for (PCB pcb1: processManager.getBlockedQueue()) {
                if (pcb1.getProcess().getInterruption().isResolved(ct)) {
                    readys.add(pcb1);
                }
            }
            for (PCB pcb1: readys) {
                processManager.toReady(pcb1);
                pcb1.getProcess().setInterrupted(false);
            }
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
                    pc.advance();
                    sentence = p.getCode().getSentenceByAddress(pc.getCurrentAddress());
                }
                try {
                    Class[] argTypes =  new Class[]{Cpu.class, PCB.class, sentence.getArguments().getClass(), Integer.class};
                    Method m = syscall.getClass().getDeclaredMethod(sentence.getFunction(), argTypes);
                    int t = (int) m.invoke(syscall, cpu, pcb, sentence.getArguments(), 200);
                    sleep(t);
                } catch (NoSuchMethodException e) {
                    p.setErrored(true);
                    e.printStackTrace();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                processManagerLock.lock();
                if (p.isFinished()) {
                    processManager.removeProcess(pcb);
                } else if (p.isErrored()) {
                    processManager.removeProcess(pcb);
                } else if (!p.isInterrupted()) {
                    pc.advance();
                    processManager.toReady(pcb);
                } else {
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
}
