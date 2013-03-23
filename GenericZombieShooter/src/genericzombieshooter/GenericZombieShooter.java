package genericzombieshooter;

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * 
 * @author Darin Beaudreau
 */
public class GenericZombieShooter extends JApplet {
    private static final float VERSION = 0.65f;
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Generic Zombie Shooter v" + VERSION);
        GZSFramework framework = new GZSFramework();
        
        frame.add(framework.canvas);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
