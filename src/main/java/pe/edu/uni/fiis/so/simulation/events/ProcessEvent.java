package pe.edu.uni.fiis.so.simulation.events;

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
        if (pcb.getProcess() != null) {
            putValue("name", pcb.getProcess().getName());
            putValue("errored", pcb.getProcess().isErrored());
        } else {
            putValue("name", "untitled");
        }
        putValue("avgRunning", pcb.getAvgRunningTime());
        putValue("minRunning", pcb.getMinRunningTime());
        putValue("maxRunning", pcb.getMaxRunningTime());
        putValue("avgWaiting", pcb.getAvgWaitingTime());
        putValue("minWaiting", pcb.getMinWaitingTime());
        putValue("maxWaiting", pcb.getMaxWaitingTime());
        putValue("avgReady", pcb.getAvgReadyTime());
        putValue("minReady", pcb.getMinReadyTime());
        putValue("maxReady", pcb.getMaxReadyTime());
    }
}
