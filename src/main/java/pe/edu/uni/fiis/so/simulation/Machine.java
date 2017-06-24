package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.memory.Memory;
import pe.edu.uni.fiis.so.util.GlobalConfig;
import pe.edu.uni.fiis.so.util.Lib;

/**
 * Created by vcueva on 6/24/17.
 */
public class Machine {
    private Cpu[] cpus;
    private Memory memory;

    public Machine() {
        int nCpus = GlobalConfig.getInt("machine.cpus", 1);
        Lib.assertTrue(nCpus > 0);

        cpus = new Cpu[nCpus];
        for (int i = 0; i < nCpus; i++) {
            cpus[i] = new Cpu();
        }

        long size = GlobalConfig.getLong("memory.size", Memory.DEFAULT_MEMORY_SIZE);
        int pageSize = GlobalConfig.getInt("memory.pageSize", Memory.DEFAULT_PAGE_SIZE);
        memory = new Memory(size, pageSize);
    }
}
