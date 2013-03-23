package genericzombieshooter;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.Particle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
            background = ImageIO.read(getClass().getResource("/resources/images/GZS_Background.png"));
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

        try {
            // If there are any projectiles left to draw...
            if (!framework.getProjectiles().isEmpty()) {
                // Draw all projectiles in the projectiles list.
                for (Particle p : framework.getProjectiles()) {
                    g2d.setTransform(p.getTransform());
                    g2d.drawImage(p.getImage(), (int) p.x, (int) p.y, null);
                }
            }
        } catch (RuntimeException r) {
        }

        { // Begin drawing the player.
            Player p = framework.getPlayer();

            g2d.setTransform(framework.getPlayer().getTransform());
            g2d.drawImage(p.getImage(), (int) p.x, (int) p.y, null);
        } // End drawing the player.

        try {
            // If there are any zombies left to draw...
            if (!framework.getZombies().isEmpty()) {
                // Draw all zombies within the zombies list.
                for (Zombie z : framework.getZombies()) {
                    g2d.setTransform(z.getTransform());
                    z.getImage().draw(g2d);
                }
            }
        } catch (RuntimeException r) {
        }

        g2d.setTransform(saved);

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
        g2d.drawString(("Spawn Time: " + framework.getSpawnTime()), 10, 45);
    }
}
