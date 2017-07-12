package pe.edu.uni.fiis.so.simulation.events;

import java.util.List;

/**
 * Created by vcueva on 7/11/17.
 */
public class MemoryEvent extends SimulationEvent {

    public static final int ALLOC = 1;
    public static final int FREE = 2;

    public MemoryEvent(int type, int pid, List<Integer> pages) {
        super();
        putValue("type", type);
        putValue("pages", pages);
        putValue("pid", pid);
    }
}
