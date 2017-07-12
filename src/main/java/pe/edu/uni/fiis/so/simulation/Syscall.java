package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.simulation.process.*;
import pe.edu.uni.fiis.so.simulation.process.Process;
import pe.edu.uni.fiis.so.simulation.process.interrupts.*;
import pe.edu.uni.fiis.so.simulation.services.DiscServiceRequest;
import pe.edu.uni.fiis.so.simulation.services.MemoryServiceRequest;
import pe.edu.uni.fiis.so.simulation.services.NetworkServiceRequest;
import pe.edu.uni.fiis.so.simulation.services.StartupServiceRequest;
import pe.edu.uni.fiis.so.statistic.StatisticParser;
import pe.edu.uni.fiis.so.util.ArgumentParser;
import pe.edu.uni.fiis.so.util.GlobalConfig;
import pe.edu.uni.fiis.so.util.SizeParser;
import pe.edu.uni.fiis.so.util.TimeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by vcueva on 6/28/17.
 */
public class Syscall {
    private Kernel kernel;

    public Syscall(Kernel kernel) {
        this.kernel = kernel;
    }

    public int jump(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() != 1) {
            pcb.getProcess().setErrored(true);
            return 5;
        }
        ProgramCounter pc = pcb.getProgramCounter();
        Code code = pcb.getProcess().getCode();
        Sentence sntc = code.getSentenceByLabel(args.get(0));
        pc.setDelta(sntc.getAddress() - pc.getCurrentAddress());
        return 10;
    }

    public int sleep(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        // System.out.println("sleep here");
        if (pcb.getProcess().getInterruption() != null) {
            pcb.getProcess().setInterruption(null);
            return 1;
        }

        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 5;
        }

        long currentTime = kernel.getMachine().getClock().getAbsoluteTime();
        SleepInterruption si = new SleepInterruption(currentTime, TimeParser.parse(args));
        pcb.getProcess().setInterrupted(true);
        pcb.getProcess().setInterruption(si);
        return 10;
    }

    public int runSysExe(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (pcb.getPid() != 1 || args.size() != 1) {
            pcb.getProcess().setErrored(true);
            return 5;
        }

        ArrayList<String> lines = kernel.getLinesFromFile("bin/" + args.get(0));
        if (lines == null) {
            return 10;
        }

        PCB myPcb = new PCB();
        myPcb.setPriority(-20);
        kernel.getProcessManagerLock().lock();
        kernel.getProcessManager().addProcess(myPcb);
        kernel.getProcessManagerLock().unlock();

        Code code = new Code();
        code.load(lines);
        Process process = new Process();
        process.setCode(code);
        myPcb.setProcess(process);

        long velr = GlobalConfig.getLong("disc.readSpeed", 50<<20);
        _sleep((1000*process.getSize()) / velr, cpu);
        kernel.getMemoryManager().malloc(process.getSize(), myPcb.getPid());

        kernel.getProcessManagerLock().lock();
        kernel.getProcessManager().toReady(myPcb);
        kernel.getProcessManagerLock().unlock();

        return 10;
    }

    public int end(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        // System.out.println("end here");
        pcb.getProcess().setFinished(true);
        return 10;
    }

    public int print(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 5;
        }
        System.out.println(args.get(0));
        return 10;
    }

    public int runExe(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() != 1) {
            pcb.getProcess().setErrored(true);
            return 5;
        }

        if (pcb.getProcess().getInterruption() != null) {
            if (pcb.getProcess().getInterruption().result() == InterruptionConstantes.ERROR) {
                pcb.getProcess().setErrored(true);
            } else {
                pcb.getProcess().setInterruption(null);
            }
        } else {
            RunExeInterruption inte = new RunExeInterruption();
            StartupServiceRequest ssr = new StartupServiceRequest(inte, args.get(0), pcb);
            pcb.getProcess().setInterruption(inte);
            pcb.getProcess().setInterrupted(true);
            _sleep(5, cpu);
            kernel.getStartupServiceQueue().add(ssr);
        }

        return 10;
    }

    public int startUpService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        Queue<StartupServiceRequest> ssq = kernel.getStartupServiceQueue();
        if (ssq.isEmpty()) {
            return 5;
        }

        StartupServiceRequest poll = ssq.poll();

        ArrayList<String> lines = kernel.getLinesFromFile("user/" + poll.getProgram());
        if (lines == null) {
            poll.getInterruption().setInterruptionResult(InterruptionConstantes.ERROR);
            poll.getInterruption().markAsResolved();
            return 10;
        }

        PCB myPcb = new PCB();
        kernel.getProcessManagerLock().lock();
        kernel.getProcessManager().addProcess(myPcb);
        kernel.getProcessManagerLock().unlock();

        try {
            Code code = new Code();
            code.load(lines);
            Process process = new Process();
            process.setCode(code);
            myPcb.setProcess(process);

            if (process.getSize() > kernel.getMachine().getMemory().getFreeMemorySize()) {
                throw new Exception("Insuficient memory");
            }

            long velr = GlobalConfig.getLong("disc.readSpeed", 50<<20);
            List<Integer> pageTable;
            _sleep((1000*process.getSize()) / velr, cpu);
            kernel.getMemoryLock().lock();
            pageTable = kernel.getMemoryManager().malloc(process.getSize(), myPcb.getPid());
            kernel.getMemoryLock().unlock();

            if (pageTable == null) {
                throw new Exception("Does not possible to assign memory.");
            }

            kernel.getProcessManagerLock().lock();
            kernel.getProcessManager().toReady(myPcb);
            kernel.getProcessManagerLock().unlock();

            poll.getInterruption().setInterruptionResult(InterruptionConstantes.SUCCESS);
        } catch (Exception e) {
            // e.printStackTrace();
            kernel.getProcessManagerLock().lock();
            kernel.getProcessManager().removeProcess(myPcb);
            kernel.getProcessManagerLock().unlock();
            poll.getInterruption().setInterruptionResult(InterruptionConstantes.ERROR);
        }

        return 10;
    }

    public int memoryService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        Queue<MemoryServiceRequest> msq = kernel.getMemoryServiceQueue();
        if (msq.isEmpty()) {
            return 5;
        }
        MemoryServiceRequest poll = msq.poll();
        List<Integer> pages;
        kernel.getMemoryLock().lock();
        pages = kernel.getMemoryManager().malloc(poll.getSize(), poll.getPcb().getPid());
        kernel.getMemoryLock().unlock();

        int aditional = 0;
        if (pages == null) {
            poll.getInterruption().setInterruptionResult(InterruptionConstantes.ERROR);
            poll.getInterruption().markAsResolved();
        } else {
            poll.getInterruption().setInterruptionResult(InterruptionConstantes.SUCCESS);
            poll.getInterruption().markAsResolved();
            aditional += poll.getSize() / (8 << 20);
        }

        return 10 + aditional;
    }

    public int diskService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        Queue<DiscServiceRequest> dsq = kernel.getDiscServiceQueue();
        if (dsq.isEmpty()) {
            return 5;
        }

        Map<String, Object> memory = pcb.getProcess().getMemory();
        if (!memory.containsKey("state")) {
            memory.put("state", 0);
        }
        if (!memory.containsKey("avance")) {
            memory.put("avance", 0L);
        }

        DiscServiceRequest peek = dsq.peek();
        if (memory.get("state").equals(0)) {
            memory.put("avance", 0L);
            memory.put("state", 1);
        }
        long ava = (long) memory.get("avance");
        long left = peek.getSize() - ava;
        long v;
        if (peek.getType() == DiscServiceRequest.DISC_READ) {
            v = GlobalConfig.getInt("disc.readSpeed", 50000000) / 1000;
        } else {
            v = GlobalConfig.getInt("disc.writeSpeed", 20000000) / 1000;
        }
        long t = left / v;
        if (t + 10 > maxTime) {
            _sleep(maxTime - 10, cpu);
            memory.put("avance", ava + v*(maxTime - 10));
            pcb.getProgramCounter().setDelta(0);
        } else {
            _sleep(t, cpu);
            memory.put("state", 0);
            dsq.poll();
            peek.getInterruption().setInterruptionResult(InterruptionConstantes.SUCCESS);
            peek.getInterruption().markAsResolved();
        }

        return 10;
    }

    public int networkService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        Queue<NetworkServiceRequest> nsq = kernel.getNetworkServiceQueue();
        if (nsq.isEmpty()) {
            return 5;
        }

        Map<String, Object> memory = pcb.getProcess().getMemory();
        if (!memory.containsKey("state")) {
            memory.put("state", 0);
        }

        NetworkServiceRequest peek = nsq.peek();
        if (memory.get("state").equals(0)) {
            memory.put("avance", 0L);
            memory.put("state", 1);
        }
        long ava = (long) memory.get("avance");
        long left = peek.getSize() - ava;
        long v;
        if (peek.getType() == peek.NET_DOWNLOAD) {
            v = GlobalConfig.getInt("net.downloadSpeed", 50000000) / 1000;
        } else {
            v = GlobalConfig.getInt("disc.uploadSpeed", 20000000) / 1000;
        }
        long t = left / v;

        if (t > maxTime - 10) {
            _sleep(maxTime - 10, cpu);
            memory.put("avance", ava + v*(maxTime - 10));
            pcb.getProgramCounter().setDelta(0);
        } else {
            _sleep(t, cpu);
            memory.put("state", 0);
            peek.getInterruption().setInterruptionResult(InterruptionConstantes.SUCCESS);
            peek.getInterruption().markAsResolved();
            nsq.poll();
        }

        return 10;
    }

    public int shellService(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        Queue<String> scq = Simulation.getInstance().getShellCommandsQueue();

        if (scq.isEmpty()) {
            return 5;
        }

        List<String> parsed = ArgumentParser.parse(scq.poll());
        if (parsed.size() == 0) {
            return 10;
        }

        if (parsed.get(0).equals("run")) {
            if (parsed.size() == 1) {
                return 10;
            } else {
                RunExeInterruption inte = new RunExeInterruption();
                kernel.getStartupServiceQueue().add(new StartupServiceRequest(inte, parsed.get(1), pcb));
                return 10;
            }
        }

        return 10;
    }

    public int rsleep(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        // System.out.println("sleep here");
        if (pcb.getProcess().getInterruption() != null) {
            pcb.getProcess().setInterruption(null);
            return 1;
        }

        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 5;
        }

        long currentTime = kernel.getMachine().getClock().getAbsoluteTime();
        long duration =  StatisticParser.parse(args.get(0)).nextLongRandom();

        SleepInterruption si = new SleepInterruption(currentTime, duration);
        pcb.getProcess().setInterrupted(true);
        pcb.getProcess().setInterruption(si);
        return 10;
    }

    public int discRead(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 2;
        }
        InterruptionInterface inte = pcb.getProcess().getInterruption();
        if (inte != null) {
            pcb.getProcess().setInterruption(null);
            return 3;
        } else {
            DiscInterruption dinte = new DiscInterruption();
            DiscServiceRequest req = new DiscServiceRequest(SizeParser.parse(args), DiscServiceRequest.DISC_READ, dinte, pcb);
            pcb.getProcess().setInterruption(dinte);
            pcb.getProcess().setInterrupted(true);
            kernel.getDiscServiceQueue().add(req);
        }

        return 5;
    }

    public int discWrite(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 2;
        }
        InterruptionInterface inte = pcb.getProcess().getInterruption();
        if (inte != null) {
            pcb.getProcess().setInterruption(null);
            return 3;
        } else {
            DiscInterruption dinte = new DiscInterruption();
            DiscServiceRequest req = new DiscServiceRequest(SizeParser.parse(args), DiscServiceRequest.DISC_WRITE, dinte, pcb);
            pcb.getProcess().setInterruption(dinte);
            pcb.getProcess().setInterrupted(true);
            kernel.getDiscServiceQueue().add(req);
        }

        return 5;
    }

    public int netUpload(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() <= 1) {
            pcb.getProcess().setErrored(true);
            return 2;
        }
        InterruptionInterface inte = pcb.getProcess().getInterruption();
        if (inte != null) {
            pcb.getProcess().setInterruption(null);
            return 3;
        } else {
            NetworkInterruption nInte = new NetworkInterruption();
            NetworkServiceRequest req = new NetworkServiceRequest(nInte, pcb,
                    NetworkServiceRequest.NET_UPLOAD, args.get(0),
                    SizeParser.parse(args.subList(1, args.size())));

            pcb.getProcess().setInterruption(nInte);
            pcb.getProcess().setInterrupted(true);
            kernel.getNetworkServiceQueue().add(req);
        }

        return 5;
    }

    public int netDownload(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() <= 1) {
            pcb.getProcess().setErrored(true);
            return 2;
        }
        InterruptionInterface inte = pcb.getProcess().getInterruption();
        if (inte != null) {
            pcb.getProcess().setInterruption(null);
            return 3;
        } else {
            NetworkInterruption nInte = new NetworkInterruption();
            NetworkServiceRequest req = new NetworkServiceRequest(nInte, pcb,
                    NetworkServiceRequest.NET_DOWNLOAD, args.get(0),
                    SizeParser.parse(args.subList(1, args.size())));

            pcb.getProcess().setInterruption(nInte);
            pcb.getProcess().setInterrupted(true);
            kernel.getNetworkServiceQueue().add(req);
        }

        return 5;
    }

    public int run(Cpu cpu, PCB pcb, ArrayList<String> args, Integer maxTime) {
        if (args.size() == 0) {
            pcb.getProcess().setErrored(true);
            return 2;
        }

        Map<String, Object> memory = pcb.getProcess().getMemory();
        if (!memory.containsKey("state")) {
            memory.put("state", 0);
        }

        if (memory.get("state").equals(0)) {
            memory.put("state", 1);
            memory.put("avance", 0L);
        }

        long ava = (long) memory.get("avance");
        long left = TimeParser.parse(args) - ava;
        if (left > maxTime - 10) {
            _sleep(maxTime - 10, cpu);
            memory.put("avance", left + maxTime - 10);
            pcb.getProgramCounter().setDelta(0);
            return 10;
        } else {
            _sleep(left, cpu);
            memory.put("state", 0);
        }

        return 10;
    }

    private void _sleep(long t, Cpu cpu) {
        try {
            t *= kernel.getMachine().getClock().getFactor();
            while (t > 0) {
                Thread.sleep(Math.min(t, 100));
                cpu.updateStatistic();
                t -= 100;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
