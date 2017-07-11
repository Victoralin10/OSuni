package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.process.Code;
import pe.edu.uni.fiis.so.simulation.process.PCB;
import pe.edu.uni.fiis.so.simulation.process.ProgramCounter;
import pe.edu.uni.fiis.so.simulation.process.Sentence;
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

    public int runExe(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("here runExe");
        return 10;
    }

    public int end(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        System.out.println("end here");
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
}
