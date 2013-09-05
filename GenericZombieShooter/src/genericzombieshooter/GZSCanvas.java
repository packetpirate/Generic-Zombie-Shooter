/**
    This file is part of Generic Zombie Shooter.

    Generic Zombie Shooter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Generic Zombie Shooter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Generic Zombie Shooter.  If not, see <http://www.gnu.org/licenses/>.
 **/
package genericzombieshooter;

import genericzombieshooter.actors.Player;
import genericzombieshooter.actors.Zombie;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
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
        this.background = Images.BACKGROUND;

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

        if(Globals.started) {
            Player player = framework.getPlayer();
            g2d.drawImage(background, 0, 0, null);
            
            // Draw Items
            framework.getItemFactory().draw(g2d);

            { // Begin drawing player and ammo.
                Iterator<Weapon> it = player.getAllWeapons().iterator();
                while(it.hasNext()) {
                    Weapon w = it.next();
                    w.drawAmmo((Graphics2D)g2d);
                }
                player.draw(g2d);
            } // End drawing player and ammo.

            g2d.setTransform(saved);
            
            { // Begin drawing zombies.
                synchronized(framework.getWave().getZombies()) {
                    Iterator<Zombie> it = framework.getWave().getZombies().iterator();
                    while(it.hasNext()) {
                        Zombie z = it.next();
                        z.draw(g2d);
                        g2d.setTransform(saved);
                    }
                }
            } // End drawing zombies.

            g2d.setTransform(saved); // Restore original transform state.
            
            { // Draw circle of light around player.
                float radius = 200.0f;
                float [] dist = {0.0f, 1.0f};
                Color [] colors = {new Color(0.0f, 0.0f, 0.0f, 0.0f), Color.BLACK};
                RadialGradientPaint p = new RadialGradientPaint(new Point2D.Double(player.getCenterX(), player.getCenterY()), 
                                                                radius, dist, colors);
                g2d.setPaint(p);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.95f));
                g2d.fillRect(0, 0, Globals.W_WIDTH, Globals.W_HEIGHT);
            } // End drawing circle of light.

            { // Draw GUI elements.
                { // Begin drawing the health bar.
                    // Draw the gray box under the HUD.
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fillRect(2, 2, (Player.MAX_HEALTH + 20), 84);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(2, 2, (Player.MAX_HEALTH + 20), 84);
                    // Draw the black bar behind the red health bar to act as a border.
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(10, 10, (Player.MAX_HEALTH + 4), 20);

                    // Only draw the red bar indicating health if player is still alive.
                    if (player.getHealth() > 0) {
                        g2d.setColor(((player.isPoisoned())?new Color(39, 161, 18):new Color(209, 21, 33)));
                        g2d.fillRect(12, 12, player.getHealth(), 16);
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(("HP: " + player.getHealth() + "/" + Player.MAX_HEALTH), 15, 25);
                    }
                } // End drawing the health bar.
                // Draw status messages.
                g2d.setColor(Color.BLACK);
                g2d.drawString(("Cash: $" + player.getCash()), 10, 42);
                g2d.drawString(("Lives: " + player.getLives()), 10, 55);
                g2d.drawString(("Ammo: " + player.getWeapon().getAmmoLeft() + "/" + player.getWeapon().getMaxAmmo()),
                                10, 68);
                if(player.isPoisoned()) {
                    long timeLeft = player.getPoisonEndTime() - System.currentTimeMillis();
                    g2d.setColor(new Color(39, 161, 18));
                    g2d.drawString(("Poisoned for " + (timeLeft / 1000) + "s!"), 10, 81);
                }
                framework.getLoadout().draw((Graphics2D)g2d);
                
                g2d.setColor(Color.WHITE);
                Font font = new Font("Impact", Font.PLAIN, 20);
                FontMetrics metrics = g2d.getFontMetrics(font);
                g2d.setFont(font);
                if(!Globals.waveInProgress) {
                    long timeLeft = Globals.nextWave - System.currentTimeMillis();
                    String s = "Next wave in " + ((timeLeft / 1000) + 1) + "...";
                    int w = metrics.stringWidth(s);
                    g2d.drawString(s, (Globals.W_WIDTH - (w + 20)), 24);
                } else {
                    String s = "Current Wave: " + framework.getWave().getWaveNumber();
                    int w = metrics.stringWidth(s);
                    g2d.drawString(s, (Globals.W_WIDTH - (w + 20)), 24);
                }
                g2d.setFont(null);
            } // End drawing GUI elements.
        } else {
            g2d.drawImage(Images.START_SCREEN, 0, 0, null);
        }
    }
}
