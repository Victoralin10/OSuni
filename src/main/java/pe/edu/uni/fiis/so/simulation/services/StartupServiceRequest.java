package pe.edu.uni.fiis.so.simulation.services;

import pe.edu.uni.fiis.so.simulation.process.PCB;
import pe.edu.uni.fiis.so.simulation.process.interrupts.RunExeInterruption;

/**
 * Created by vcueva on 7/11/17.
 */
public class StartupServiceRequest {
    private RunExeInterruption interruption;
    private String program;
    private PCB pcb;

    public StartupServiceRequest(RunExeInterruption interruption, String program, PCB pcb) {
        this.interruption = interruption;
        this.program = program;
        this.pcb = pcb;
    }

    public RunExeInterruption getInterruption() {
        return interruption;
    }

    public String getProgram() {
        return program;
    }

    public PCB getPcb() {
        return pcb;
    }
}
