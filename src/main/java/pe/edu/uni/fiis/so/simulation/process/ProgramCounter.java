package pe.edu.uni.fiis.so.simulation.process;

/**
 * Created by vcueva on 6/28/17.
 */
public class ProgramCounter {

    private Integer currentAddress;
    private Integer delta;

    ProgramCounter() {
        currentAddress = 2;
        delta = 1;
    }

    public Integer getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Integer currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Integer getDelta() {
        return delta;
    }

    public void setDelta(Integer delta) {
        this.delta = delta;
    }

    public void advance() {
        currentAddress += delta;
        delta = 1;
    }
}
