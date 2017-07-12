package pe.edu.uni.fiis.so.simulation.policies;

import pe.edu.uni.fiis.so.simulation.process.PCB;

import java.util.List;

/**
 * Created by vcueva on 7/12/17.
 */
public class SjfPolicy implements PolicyInterface {

    private boolean priority;

    public SjfPolicy() {
        this.priority = false;
    }

    public SjfPolicy(boolean priority) {
        this.priority = priority;
    }

    @Override
    public PCB next(List<PCB> readyQueue) {
        if (readyQueue.size() == 0) {
            return null;
        }

        PCB ans = null;
        for (PCB pcb: readyQueue) {
            if (ans == null) {
                ans = pcb;
            } else if (ans.getAvgRunningTime() > pcb.getAvgRunningTime()) {
                ans = pcb;
            }
        }

        return ans;
    }
}
