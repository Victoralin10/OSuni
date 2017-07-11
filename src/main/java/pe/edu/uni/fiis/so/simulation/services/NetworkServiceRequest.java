package pe.edu.uni.fiis.so.simulation.services;

import pe.edu.uni.fiis.so.simulation.process.PCB;
import pe.edu.uni.fiis.so.simulation.process.interrupts.NetworkInterruption;

/**
 * Created by vcueva on 7/11/17.
 */
public class NetworkServiceRequest {
    public final int NET_UPLOAD = 1;
    public final int NET_DOWNLOAD = 2;

    private NetworkInterruption interruption;
    private PCB pcb;
    private int type;
    private String direction;
    private long size;

    public NetworkServiceRequest(NetworkInterruption interruption, PCB pcb, int type, String direction, long size) {
        this.interruption = interruption;
        this.pcb = pcb;
        this.type = type;
        this.direction = direction;
        this.size = size;
    }

    public NetworkInterruption getInterruption() {
        return interruption;
    }

    public PCB getPcb() {
        return pcb;
    }

    public int getType() {
        return type;
    }

    public String getDirection() {
        return direction;
    }

    public long getSize() {
        return size;
    }
}
