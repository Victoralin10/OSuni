package pe.edu.uni.fiis.so.simulation.policies;

import pe.edu.uni.fiis.so.simulation.process.PCB;

import java.util.List;

/**
 * Created by vcueva on 6/28/17.
 */
public interface PolicyInterface {

    PCB next(List<PCB> readyQueue);
}
