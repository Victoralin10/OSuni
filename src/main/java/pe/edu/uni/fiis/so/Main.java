package pe.edu.uni.fiis.so;

import pe.edu.uni.fiis.so.simulation.Simulation;
import pe.edu.uni.fiis.so.util.GlobalConfig;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class Main {

    /**
     * Here starts the system.
     *
     * @param args System args
     */
    public static void main(String[] args) {
        GlobalConfig.load("config.ini");
        System.out.println("Hello World");
        Simulation.getInstance().start();

        try {
            Thread.sleep(10*1000);
            Simulation.getInstance().stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
