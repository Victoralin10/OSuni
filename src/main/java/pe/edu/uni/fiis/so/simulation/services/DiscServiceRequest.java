package pe.edu.uni.fiis.so.simulation.services;

import pe.edu.uni.fiis.so.simulation.process.PCB;
import pe.edu.uni.fiis.so.simulation.process.interrupts.DiscInterruption;

/**
 * Created by vcueva on 7/11/17.
 */
public class DiscServiceRequest {
    public static final int DISC_WRITE = 1;
    public static final int DISC_READ = 2;

    private long size;
    private int type;
    private DiscInterruption interruption;
    private PCB pcb;

    public DiscServiceRequest(long size, int type, DiscInterruption interruption, PCB pcb) {
        this.size = size;
        this.type = type;
        this.interruption = interruption;
        this.pcb = pcb;
    }

    public long getSize() {
        return size;
    }

    public DiscInterruption getInterruption() {
        return interruption;
    }

    public PCB getPcb() {
        return pcb;
    }

    public int getType() {
        return type;
    }
}
