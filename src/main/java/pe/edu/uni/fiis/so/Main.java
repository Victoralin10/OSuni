package pe.edu.uni.fiis.so;

import pe.edu.uni.fiis.so.monitor.ConfigView;
import pe.edu.uni.fiis.so.util.GlobalConfig;

/**
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

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConfigView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new ConfigView().setVisible(true));
    }
}
