package pe.edu.uni.fiis.so.simulation.memory;

import java.util.List;

/**
 * Created by vcueva on 6/24/17.
 */
public class BestFitManager implements MemoryManagerInterface {

    @Override
    public List<Integer> malloc(int size, int pid, boolean readOnly) {
        return null;
    }

    @Override
    public boolean free(List<Integer> pages) {
        return false;
    }

    @Override
    public boolean free(int page) {
        return false;
    }

    @Override
    public void terminate(int pid) {

    }

    @Override
    public void terminate(int pid, List<Integer> pageTable) {

    }
}
