package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.process.*;
import pe.edu.uni.fiis.so.simulation.process.Process;
import pe.edu.uni.fiis.so.simulation.process.interrupts.SleepInterruption;
import pe.edu.uni.fiis.so.util.TimeParser;

import java.util.ArrayList;

/**
 * Created by vcueva on 6/28/17.
 */
public class Syscall {
    private Kernel kernel;

    public Syscall(Kernel kernel) {
        this.kernel = kernel;
    }

    public int jump(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() != 1) {
            pcb.getProcess().setErrored(true);
            return 5;
        }
        ProgramCounter pc = pcb.getProgramCounter();
        Code code = pcb.getProcess().getCode();
        Sentence sntc = code.getSentenceByLabel(args.get(0));
        pc.setDelta(sntc.getAddress() - pc.getCurrentAddress());
        return 10;
    }

    public int sleep(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("sleep here");
        if (pcb.getProcess().getInterruption() != null) {
            pcb.getProcess().setInterruption(null);
            return 1;
        }

        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 5;
        }

        long currentTime = kernel.getMachine().getClock().getAbsoluteTime();
        SleepInterruption si = new SleepInterruption(currentTime, TimeParser.parse(args));
        pcb.getProcess().setInterrupted(true);
        pcb.getProcess().setInterruption(si);
        return 10;
    }

    public int runSysExe(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (pcb.getPid() != 1 || args.size() != 1) {
            pcb.getProcess().setErrored(true);
            return 5;
        }

        ArrayList<String> lines = kernel.getLinesFromFile("bin/" + args.get(0));
        if (lines == null) {
            System.out.println("null");
            return 10;
        }

        PCB myPcb = new PCB();
        kernel.getProcessManagerLock().lock();
        kernel.getProcessManager().addProcess(myPcb);
        kernel.getProcessManagerLock().unlock();

        Code code = new Code();
        code.load(lines);
        Process process = new Process();
        process.setCode(code);
        myPcb.setProcess(process);

        _sleep(process.getSize()/(50<<20));
        kernel.getMemoryManager().malloc(process.getSize(), myPcb.getPid());

        kernel.getProcessManagerLock().lock();
        kernel.getProcessManager().toReady(myPcb);
        kernel.getProcessManagerLock().unlock();

        return 10;
    }

    public int end(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("end here");
        pcb.getProcess().setFinished(true);
        return 10;
    }

    public int print(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 5;
        }
        System.out.println(args.get(0));
        return 10;
    }

    public int startUpService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("StartupService here");
        return 10;
    }

    public int memoryService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("memory service here");
        return 10;
    }

    public int diskService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("diskService here");
        return 10;
    }

    public int networkService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("networkManager");
        return 10;
    }

    private void _sleep(long t) {
        try {
            Thread.sleep(t*kernel.getMachine().getClock().getFactor());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
