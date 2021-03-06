package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.memory.Memory;
import pe.edu.uni.fiis.so.util.GlobalConfig;
import pe.edu.uni.fiis.so.util.Lib;
import pe.edu.uni.fiis.so.util.SizeParser;

/**
 * Created by vcueva on 6/24/17.
 */
public class Machine {

    private Cpu[] cpus;
    private Memory memory;
    private Kernel kernel;
    private Clock clock;

    public Machine() {
        int nCpus = GlobalConfig.getInt("machine.cpus", 1);
        Lib.assertTrue(nCpus > 0);

        kernel = new Kernel(this);
        cpus = new Cpu[nCpus];
        for (int i = 0; i < nCpus; i++) {
            cpus[i] = new Cpu();
            cpus[i].setKernel(kernel);
        }

        long size = SizeParser.parse(GlobalConfig.getString("memory.size", "8Gb"));
        int pageSize = (int) SizeParser.parse(GlobalConfig.getString("memory.pageSize", "4Mb"));
        memory = new Memory(size, pageSize);

        clock = new Clock();
    }

    public void powerOn() {
        Simulation.getInstance().log("Encendiendo la maqhina virtual.");
        clock.start();
        Simulation.getInstance().log("Arrancando cpus");
        for (Cpu cpu : cpus) {
            cpu.start();
        }
    }

    public void powerOff() {
        kernel.powerOffSignal();
        clock.stop();
    }

    public Memory getMemory() {
        return memory;
    }

    public Cpu[] getCpus() {
        return cpus;
    }

    public Clock getClock() {
        return clock;
    }
}
