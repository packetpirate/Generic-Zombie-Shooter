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
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.components.StoreWindow;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

/**
 * Contains drawing methods to display game graphics on the screen.
 *
 * @author packetpirate
 */
public class GZSCanvas extends JPanel {
    // Member variables.
    private GZSFramework framework;
    private StoreWindow store;
    private BufferedImage background;

    public GZSCanvas(GZSFramework framework, StoreWindow store) {
        this.framework = framework;
        this.store = store;
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
            if(!Globals.deathScreen) {
                if(!Globals.storeOpen) {
                    g2d.drawImage(background, 0, 0, null);

                    // Draw Items
                    framework.getItemFactory().draw(g2d);

                    { // Begin drawing player and ammo.
                        Stroke oldStroke = g2d.getStroke();
                        Iterator<Weapon> it = player.getWeaponsMap().values().iterator();
                        while(it.hasNext()) {
                            Weapon w = it.next();
                            w.drawAmmo((Graphics2D)g2d);
                        }
                        player.draw(g2d);
                        g2d.setStroke(oldStroke);
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
                        BufferedImage shadowBuffer = new BufferedImage(Globals.W_WIDTH, Globals.W_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D sg = (Graphics2D)shadowBuffer.getGraphics();

                        sg.setComposite(AlphaComposite.Src);
                        sg.setColor(new Color(0.0f, 0.0f, 0.0f, 0.95f));
                        sg.fillRect(0, 0, Globals.W_WIDTH, Globals.W_HEIGHT);
                        sg.setComposite(AlphaComposite.DstIn);

                        player.getLightSource().draw(sg);

                        // Draw the light sources from flares.
                        if(player.hasWeapon(Globals.FLARE.getName())) {
                            List<LightSource> lights = player.getWeapon(Globals.FLARE.getName()).getLights();
                            synchronized(lights) {
                                if(!lights.isEmpty()) {
                                    Iterator<LightSource> it = lights.iterator();
                                    while(it.hasNext()) {
                                        LightSource ls = it.next();
                                        if(ls.isAlive()) ls.draw(sg);
                                    }
                                }
                            }
                        }

                        g2d.drawImage(shadowBuffer, 0, 0, null);

                        sg.dispose();
                    } // End drawing circle of light.

                    { // Draw GUI elements.
                        g2d.setStroke(new BasicStroke(1));
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
                    // Draw the store window.
                    this.store.draw(g2d);
                }
            } else {
                // Draw the death screen.
                g2d.drawImage(Images.DEATH_SCREEN, 0, 0, null);
                
                Rectangle2D.Double statPane = new Rectangle2D.Double(((Globals.W_WIDTH / 2) - 150), 
                                                                      (Globals.W_HEIGHT / 2), 
                                                                       300, 200);
                g2d.setColor(Color.DARK_GRAY);
                g2d.fill(statPane);
                g2d.setColor(Color.GRAY);
                g2d.draw(statPane);
                
                long currentTime = System.currentTimeMillis();
                
                g2d.setColor(Color.WHITE);
                Font f = new Font("Impact", Font.BOLD, 20);
                FontMetrics metrics = g2d.getFontMetrics(f);
                g2d.setFont(f);
                { // Draw label.
                    String s = "Final Report";
                    int x = (int)((Globals.W_WIDTH / 2) - (metrics.stringWidth(s) / 2));
                    int y = (int)(statPane.y + 30);
                    g2d.drawString(s, x, y);
                } // End of label drawing.
                
                f = new Font("Arial", Font.PLAIN, 12);
                metrics = g2d.getFontMetrics(f);
                g2d.setFont(f);
                
                if(currentTime >= (player.getDeathTime() + 1000)) {
                    int killCount = player.killCount;
                    String s = ("Zombies Killed: " + killCount);
                    int x = (int)((Globals.W_WIDTH / 2) - (metrics.stringWidth(s) / 2));
                    int y = (int)(statPane.y + 55);
                    g2d.drawString(s, x, y);
                }
                
                if(currentTime >= (player.getDeathTime() + 2000)) {
                    int wave = framework.getWave().getWaveNumber();
                    String s = ("Ending Wave: " + wave);
                    int x = (int)((Globals.W_WIDTH / 2) - (metrics.stringWidth(s) / 2));
                    int y = (int)(statPane.y + 75);
                    g2d.drawString(s, x, y);
                }
                
                if(currentTime >= (player.getDeathTime() + 3000)) {
                    int medkitsUsed = player.medkitsUsed;
                    String s = ("Medkits Used: " + medkitsUsed);
                    int x = (int)((Globals.W_WIDTH / 2) - (metrics.stringWidth(s) / 2));
                    int y = (int)(statPane.y + 95);
                    g2d.drawString(s, x, y);
                }
                
                if(currentTime >= (player.getDeathTime() + 4000)) {
                    int ammoUsed = player.ammoCratesUsed;
                    String s = ("Ammo Crates Used: " + ammoUsed);
                    int x = (int)((Globals.W_WIDTH / 2) - (metrics.stringWidth(s) / 2));
                    int y = (int)(statPane.y + 115);
                    g2d.drawString(s, x, y);
                }
                
                f = new Font("Impact", Font.BOLD, 20);
                metrics = g2d.getFontMetrics(f);
                g2d.setFont(f);
                
                if(currentTime >= (player.getDeathTime() + 5000)) {
                    String s = "Click to Continue";
                    int x = (int)((Globals.W_WIDTH / 2) - (metrics.stringWidth(s) / 2));
                    int y = (int)((statPane.y + statPane.height) - 10);
                    g2d.drawString(s, x, y);
                }
            }
        } else {
            g2d.drawImage(Images.START_SCREEN, 0, 0, null);
        }
        g2d.dispose();
    }
}
