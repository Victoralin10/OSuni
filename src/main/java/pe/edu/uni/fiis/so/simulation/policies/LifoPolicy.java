package pe.edu.uni.fiis.so.simulation.policies;

import pe.edu.uni.fiis.so.simulation.process.PCB;

import java.util.List;

/**
 * Created by vcueva on 7/12/17.
 */
public class LifoPolicy implements PolicyInterface {
    private boolean priority;

    public LifoPolicy() {
        this.priority = false;
    }

    public LifoPolicy(boolean priority) {
        this.priority = priority;
    }

    @Override
    public PCB next(List<PCB> readyQueue) {
        if (readyQueue.isEmpty()) {
            return null;
        }
        return readyQueue.get(readyQueue.size() - 1);
    }
}
