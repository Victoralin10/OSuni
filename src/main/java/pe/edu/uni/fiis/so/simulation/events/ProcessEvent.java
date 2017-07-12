package pe.edu.uni.fiis.so.simulation.events;

import pe.edu.uni.fiis.so.simulation.Kernel;
import pe.edu.uni.fiis.so.simulation.process.PCB;

/**
 * Created by vcueva on 7/11/17.
 */
public class ProcessEvent extends SimulationEvent {
    public ProcessEvent(PCB pcb) {
        super();
        putValue("pid", pcb.getPid());
        putValue("newState", pcb.getProcessStatus());
        putValue("cpu", pcb.getCpuNumber());
        putValue("time", Kernel.instance.getMachine().getClock().getAbsoluteTime());
        putValue("priority", pcb.getPriority());
    }
}
