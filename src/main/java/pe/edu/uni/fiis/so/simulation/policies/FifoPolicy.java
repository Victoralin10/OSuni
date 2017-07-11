package pe.edu.uni.fiis.so.simulation.policies;

import pe.edu.uni.fiis.so.simulation.process.PCB;

import java.util.List;

/**
 * Created by vcueva on 6/28/17.
 */
public class FifoPolicy implements PolicyInterface {

    private boolean priority;

    public FifoPolicy() {
        this.priority = false;
    }

    public FifoPolicy(boolean priority) {
        this.priority = priority;
    }

    @Override
    public PCB next(List<PCB> readyQueue) {
        if (readyQueue.isEmpty()) {
            return null;
        }
        if (priority) {
            PCB ans = null;
            for (PCB pcb : readyQueue) {
                if (ans == null) {
                    ans = pcb;
                } else if (ans.getPriority() > pcb.getPriority()) {
                    ans = pcb;
                }
            }
            return ans;
        }
        return readyQueue.get(0);
    }
}
