package genericzombieshooter;

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * 
 * @author Darin Beaudreau
 */
public class GenericZombieShooter extends JApplet {
    private static final float VERSION = 0.6f;
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Generic Zombie Shooter v" + VERSION);
        Canvas canvas = new Canvas();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(canvas);
        frame.setVisible(true);
        frame.pack();
    }
}
