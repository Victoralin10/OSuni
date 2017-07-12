package pe.edu.uni.fiis.so.simulation.memory;

import pe.edu.uni.fiis.so.simulation.Simulation;
import pe.edu.uni.fiis.so.simulation.events.MemoryEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vcueva on 6/24/17.
 */
public class FirstFitManager implements MemoryManagerInterface {

    private Memory memory;

    public FirstFitManager(Memory memory) {
        this.memory = memory;
    }

    @Override
    public List<Integer> malloc(long size, int pid) {
        if (size > memory.getFreeMemorySize()) {
            return null;
        }
        ArrayList<Integer> ans = new ArrayList<>();
        int[] map = memory.getMap();
        for (int i = 0; i < memory.getTotalPages() && size > 0; i++) {
            if (map[i] < 0) {
                ans.add(i);
                map[i] = pid;
                size -= memory.getPageSize();
            }
        }
        memory.setFreeMemorySize(memory.getFreeMemorySize() - memory.getPageSize() * ans.size());
        Simulation.getInstance().dispatchEvent("memory.update", new MemoryEvent(MemoryEvent.ALLOC, pid, ans));
        return ans;
    }

    @Override
    public boolean free(int pid, List<Integer> pages) {
        int[] map = memory.getMap();

        for (Integer page : pages) {
            map[page] = -1;
        }
        memory.setFreeMemorySize(memory.getFreeMemorySize() + memory.getPageSize() * pages.size());
        Simulation.getInstance().dispatchEvent("memory.update", new MemoryEvent(MemoryEvent.FREE, pid, pages));
        return true;
    }

    @Override
    public boolean free(int pid, int page) {
        int[] map = memory.getMap();
        if (pid != map[page]) {
            return false;
        }
        map[page] = -1;
        memory.setFreeMemorySize(memory.getFreeMemorySize() + memory.getPageSize());
        ArrayList<Integer> pgs = new ArrayList<>();
        pgs.add(page);
        Simulation.getInstance().dispatchEvent("memory.update", new MemoryEvent(MemoryEvent.FREE, pid, pgs));
        return true;
    }

    @Override
    public boolean free(int pid) {
        int[] map = memory.getMap();
        ArrayList<Integer> pgs = new ArrayList<>();
        for (int i = 0; i < memory.getTotalPages(); i++) {
            if (pid == map[i]) {
                map[i] = 0;
                pgs.add(i);
            }
        }
        Simulation.getInstance().dispatchEvent("memory.update", new MemoryEvent(MemoryEvent.FREE, pid, pgs));
        return true;
    }
}
