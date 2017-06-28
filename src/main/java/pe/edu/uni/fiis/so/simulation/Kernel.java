package pe.edu.uni.fiis.so.simulation;


/**
 * Created by vcueva on 6/21/17.
 */
public class Kernel {
    public static final int STATE_INSTANCIADO = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_READY = 4;

    private final Machine machine;

    private boolean powerOffSignal;
    private int state;

    public Kernel(Machine machine) {
        this.machine = machine;
        this.powerOffSignal = false;
        this.state = 0;
    }

    public void run(Cpu cpu) {
        
    }

    public void powerOffSignal() {
        powerOffSignal = true;
    }
}
