package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.process.PCB;

/**
 * Created by vcueva on 6/28/17.
 */
public class Syscall {
    private Kernel kernel;

    public Syscall(Kernel kernel) {
        this.kernel = kernel;
    }

    public void end(Cpu cpu, PCB pcb) {

    }
}
