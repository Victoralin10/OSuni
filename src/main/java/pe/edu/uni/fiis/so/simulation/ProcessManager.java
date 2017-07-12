package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.events.NewProcess;
import pe.edu.uni.fiis.so.simulation.process.PCB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vcueva on 6/28/17.
 */
public class ProcessManager {

    private boolean blocked;

    private List<PCB> processes;
    private List<PCB> readyQueue;
    private List<PCB> blockedQueue;

    public ProcessManager() {
        this.blocked = false;
        this.processes = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        this.blockedQueue = new ArrayList<>();
    }

    public boolean isBlocked() {
        return blocked;
    }

    private void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void addProcess(PCB pcb) {
        this.processes.add(pcb);
        Simulation.getInstance().dispatchEvent("process.new", new NewProcess(pcb));
    }

    public void removeProcess(PCB pcb) {
        removeProcess(pcb.getPid());
    }

    public void removeProcess(int pid) {
        int index = 0;
        for (; index < processes.size(); index++) {
            if (pid == processes.get(index).getPid()) {
                processes.get(index).setProcessStatus(PCB.FINISHED);
                break;
            }
        }
        processes.remove(index);
    }

    private PCB getProcessByPid(int pid) {
        for (PCB pcb : processes) {
            if (pcb.getPid() == pid) {
                return pcb;
            }
        }

        return null;
    }

    public void toReady(int pid) {
        toReady(getProcessByPid(pid));
    }

    public void toReady(PCB pcb) {
        if (pcb.getProcessStatus() == PCB.WAITING) {
            blockedQueue.remove(pcb);
        }

        pcb.setProcessStatus(PCB.READY);
        readyQueue.add(pcb);
    }

    public void toRunning(int pid) {
        toRunning(getProcessByPid(pid));
    }

    public void toRunning(PCB pcb) {
        assert pcb.getProcessStatus() != PCB.RUNNING;

        readyQueue.remove(pcb);
        pcb.setProcessStatus(PCB.RUNNING);
    }

    public void toWaiting(int pid) {
        toWaiting(getProcessByPid(pid));
    }

    public void toWaiting(PCB pcb) {
        blockedQueue.add(pcb);
        pcb.setProcessStatus(PCB.WAITING);
    }

    public List<PCB> getReadyQueue() {
        return readyQueue;
    }

    public List<PCB> getBlockedQueue() {
        return blockedQueue;
    }
}
