package pe.edu.uni.fiis.so.simulation.policies;

import pe.edu.uni.fiis.so.simulation.Cpu;
import pe.edu.uni.fiis.so.util.GlobalConfig;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vcueva on 6/28/17.
 */
public class PolicyManager {

    private Map<Integer, PolicyInterface> policies;

    public PolicyManager(Cpu[] cpus) {
        policies = new TreeMap<>();

        for (int i = 0; i < cpus.length; i++) {
            String policyName = GlobalConfig.getString("kernel.policy.cpu" + i, "fifo");
            PolicyInterface policy;
            if(policyName.equals("lifo")) {
                policy = new LifoPolicy();
            } else {
                policy = new FifoPolicy();
            }

            policies.put(cpus[i].getId(), policy);
        }
    }

    public PolicyInterface getPolicy(Cpu cpu) {
        return policies.get(cpu.getId());
    }
}
