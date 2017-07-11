package pe.edu.uni.fiis.so.simulation.events;

import pe.edu.uni.fiis.so.simulation.process.PCB;

/**
 * Created by vcueva on 7/11/17.
 */
public class ProcessEvent extends  SimulationEvent {
    public ProcessEvent(PCB pcb) {
        super();
        putValue("newState", pcb.getProcessStatus());
        putValue("pid", pcb.getPid());
    }
}
