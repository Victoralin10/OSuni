package pe.edu.uni.fiis.so.simulation.memory;

import java.util.List;

/**
 * Created by vcueva on 6/24/17.
 */
public class BestFitManager implements MemoryManagerInterface {

    @Override
    public List<Integer> malloc(long size, int pid) {
        return null;
    }

    @Override
    public boolean free(int pid, List<Integer> pages) {
        return false;
    }

    @Override
    public boolean free(int pid, int page) {
        return false;
    }

    @Override
    public boolean free(int pid) {
        return false;
    }
}
