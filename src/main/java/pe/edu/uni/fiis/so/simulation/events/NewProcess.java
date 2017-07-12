package pe.edu.uni.fiis.so.simulation.events;

import pe.edu.uni.fiis.so.simulation.Kernel;
import pe.edu.uni.fiis.so.simulation.process.PCB;

/**
 * Created by vcueva on 7/11/17.
 */
public class NewProcess extends SimulationEvent {

    public NewProcess(PCB pcb) {
        super();
        long time = Kernel.instance.getMachine().getClock().getAbsoluteTime();
        putValue("time", time);
        putValue("pid", pcb.getPid());
        putValue("priority", pcb.getPriority());
    }
}
