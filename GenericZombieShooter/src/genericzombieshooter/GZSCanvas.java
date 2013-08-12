package genericzombieshooter;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Contains drawing methods to display game graphics on the screen.
 *
 * @author packetpirate
 */
public class GZSCanvas extends JPanel {
    // Member variables.
    private GZSFramework framework;
    private BufferedImage background;

    public GZSCanvas(GZSFramework framework) {
        this.framework = framework;

        try {
            background = ImageIO.read(getClass().getResource("/resources/images/GZS_Background_2.png"));
        } catch (IOException io) {
            System.out.println("ERROR: File not found! Background image could not be loaded!");
        }

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(Globals.W_WIDTH, Globals.W_HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform saved = g2d.getTransform();

        g2d.drawImage(background, 0, 0, null);
        
        { // Begin drawing player and ammo.
            Player player = framework.getPlayer();
            Iterator<Weapon> it = player.getAllWeapons().iterator();
            while(it.hasNext()) {
                Weapon w = it.next();
                w.drawAmmo((Graphics2D)g2d);
            }
            g2d.setTransform(framework.getPlayer().getTransform());
            g2d.drawImage(player.getImage(), (int) player.x, (int) player.y, null);
        } // End drawing player and ammo.
        
        { // Begin drawing zombies.
            Iterator<Zombie> it = framework.getZombies().iterator();
            while(it.hasNext()) {
                Zombie z = it.next();
                g2d.setTransform(z.getTransform());
                z.getImage().draw((Graphics2D)g2d);
            }
        } // End drawing zombies.

        g2d.setTransform(saved); // Restore original transform state.

        { // Draw GUI elements.
            { // Begin drawing the health bar.
                // Draw the black bar behind the red health bar to act as a border.
                g2d.setColor(Color.BLACK);
                g2d.fillRect(10, 10, 204, 20);

                // Only draw the red bar indicating health if player is still alive.
                if (framework.getPlayer().getHealth() > 0) {
                    g2d.setColor(Color.RED);
                    g2d.fillRect(12, 12, framework.getPlayer().getHealth(), 16);
                }
            } // End drawing the health bar.
            g2d.setColor(Color.WHITE);
            g2d.drawString(("Score: " + framework.getScore()), 10, 45);
            framework.getLoadout().draw((Graphics2D)g2d);
        } // End drawing GUI elements.
    }
}
