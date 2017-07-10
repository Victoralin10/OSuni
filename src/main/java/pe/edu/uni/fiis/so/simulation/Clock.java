package pe.edu.uni.fiis.so.simulation;

import pe.edu.uni.fiis.so.util.GlobalConfig;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vcueva on 6/28/17.
 */
public class Clock extends TimerTask {

    /**
     * Default factor
     */
    private static final int DEFAULT_FACTOR = 10;
    /**
     * Period of the timer for refresh.
     */
    private static final int DEFAULT_TIMER_REFRESH_PERIOD = 25;
    /**
     * Static instance of this class.
     */
    /**
     * The factor is how much the real time is expanded in simulation time.
     */
    private int factor;

    /**
     * Stores the absolute time from the begin of the simulation.
     */
    private double absoluteTime = 0;
    /**
     * This times update the absolute time every DEFAULT_TIMER_REFRESH_PERIOD time.
     */
    private Timer timer;

    /**
     * Constructor. It is private to ensure one instance only.
     */
    public Clock() {
        this.factor = GlobalConfig.getInt("simulation.ut", DEFAULT_FACTOR);
    }

    /**
     * Getter for the <code>factor</code> property.
     *
     * @return factor
     */
    public int getFactor() {
        return factor;
    }

    /**
     * Setter to change the factor property.
     *
     * @param factor int The new factor.
     */
    public synchronized void setFactor(int factor) {
        if (factor <= 0) {
            System.err.println("Factor invalid.");
            return;
        }
        this.factor = factor;
    }

    /**
     * Convert real time to simulation time.
     *
     * @param time Time in milliseconds to convert.
     * @return Time in milliseconds converted.
     */
    public double convert(double time) {
        return time * factor;
    }

    /**
     * Convert simulation time to real time.
     *
     * @param time Time in milliseconds to convert.
     * @return Time in milliseconds converted.
     */
    public double revert(double time) {
        return time / factor;
    }

    /**
     * Handles the timer update event.
     */
    @Override
    public void run() {
        absoluteTime += (double) DEFAULT_TIMER_REFRESH_PERIOD / factor;
    }

    /**
     * Starts the timer.
     */
    public void start() {
        if (timer != null) {
            throw new Error("The timer is already running");
        }

        absoluteTime = 0;
        timer = new Timer();
        timer.schedule(this, 0, DEFAULT_TIMER_REFRESH_PERIOD);
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    /**
     * Compute the absolute time from the begin of the simulation.
     *
     * @return Time in milliseconds.
     */
    public long getAbsoluteTime() {
        return (long) absoluteTime;
    }
}
