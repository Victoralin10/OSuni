package pe.edu.uni.fiis.so.simulation.services;

import pe.edu.uni.fiis.so.simulation.process.PCB;
import pe.edu.uni.fiis.so.simulation.process.interrupts.MemoryInterruption;

/**
 * Created by vcueva on 7/11/17.
 */
public class MemoryServiceRequest {
    private long size;
    private PCB pcb;
    private MemoryInterruption interruption;

    public MemoryServiceRequest(long size, PCB pcb, MemoryInterruption interruption) {
        this.size = size;
        this.pcb = pcb;
        this.interruption = interruption;
    }

    public long getSize() {
        return size;
    }

    public PCB getPcb() {
        return pcb;
    }

    public MemoryInterruption getInterruption() {
        return interruption;
    }
}
