package pe.edu.uni.fiis.so.simulation.events;

import pe.edu.uni.fiis.so.simulation.Cpu;

/**
 * Created by vcueva on 7/11/17.
 */
public class CpuEvent extends SimulationEvent {

    public CpuEvent(Cpu cpu) {
        super();
        putValue("id", cpu.getId());
        putValue("status", cpu.getState());
        putValue("avgUsage", cpu.getAvgTimeRunning());
    }
}
