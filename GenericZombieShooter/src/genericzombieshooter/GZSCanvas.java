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
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.structures.LightSource;
import genericzombieshooter.structures.Message;
import genericzombieshooter.structures.StatusEffect;
import genericzombieshooter.structures.components.LevelScreen;
import genericzombieshooter.structures.components.StoreWindow;
import genericzombieshooter.structures.components.WeaponsLoadout;
import genericzombieshooter.structures.items.NightVision;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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
    private LevelScreen levelScreen;
    private BufferedImage background;

    public GZSCanvas(GZSFramework framework, StoreWindow store, LevelScreen levelScreen) {
        this.framework = framework;
        this.store = store;
        this.levelScreen = levelScreen;
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
                if(!Globals.storeOpen && !Globals.levelScreenOpen) {
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

                    // Draw zombies and zombie particles.
                    framework.getWave().draw(g2d);

                    g2d.setTransform(saved); // Restore original transform state.

                    { // Draw circle of light around player.
                        BufferedImage shadowBuffer = new BufferedImage(Globals.W_WIDTH, Globals.W_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D sg = (Graphics2D)shadowBuffer.getGraphics();

                        // For each light source, draw a translucent radial gradient painted rectangle over the "shadow" layer.
                        float layerOpacity = ((player.hasEffect(NightVision.EFFECT_NAME))?0.80f:0.95f);
                        sg.setComposite(AlphaComposite.Src);
                        sg.setColor(new Color(0.0f, 0.0f, 0.0f, layerOpacity));
                        sg.fillRect(0, 0, Globals.W_WIDTH, Globals.W_HEIGHT);
                        sg.setComposite(AlphaComposite.DstIn);

                        { // Draw flashlight gradient.
                            double distance = 400;
                            Polygon flashlight = new Polygon();
                            Point2D.Double startPoint = new Point2D.Double((player.getCenterX() + 4), (player.getCenterY() - 8));
                            AffineTransform.getRotateInstance(player.getTheta(), player.getCenterX(), player.getCenterY()).transform(startPoint, startPoint);
                            Point2D.Double endPoint = new Point2D.Double(startPoint.x, startPoint.y);
                            double theta = player.getTheta() - (Math.PI / 2);
                            { // Add points to flashlight polygon.
                                flashlight.addPoint((int)endPoint.x, (int)endPoint.y);
                                flashlight.addPoint((int)(endPoint.x + (distance * Math.cos(theta - Math.toRadians(25)))), 
                                                    (int)(endPoint.y + (distance * Math.sin(theta - Math.toRadians(25)))));
                                flashlight.addPoint((int)(endPoint.x + (distance * Math.cos(theta + Math.toRadians(25)))), 
                                                    (int)(endPoint.y + (distance * Math.sin(theta + Math.toRadians(25)))));
                            } // End adding points to flashlight polygon.
                            startPoint.x += (distance / 2) * Math.cos(theta);
                            startPoint.y += (distance / 2) * Math.sin(theta);
                            endPoint.x += (distance * Math.cos(theta)) * 0.75;
                            endPoint.y += (distance * Math.sin(theta)) * 0.75;
                            GradientPaint gp = new GradientPaint(startPoint, new Color(0, 0, 0, 100), endPoint, Color.WHITE);
                            sg.setPaint(gp);
                            sg.fill(flashlight);
                        } // End drawing flashlight gradient.
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
                            g2d.fillRect(2, 2, (Player.DEFAULT_HEALTH + 20), 94);
                            g2d.setColor(Color.BLACK);
                            g2d.drawRect(2, 2, (Player.DEFAULT_HEALTH + 20), 94);
                            // Draw the black bar behind the red health bar to act as a border.
                            g2d.setColor(Color.BLACK);
                            g2d.fillRect(10, 10, (Player.DEFAULT_HEALTH + 4), 20);
                            // Draw the black bar behind the experience bar to act as a border.
                            g2d.fillRect(10, 32, 154, 20);

                            // Only draw the red bar indicating health if player is still alive.
                            if (player.getHealth() > 0) {
                                int healthBarWidth = (int)(((double)player.getHealth() / (double)player.getMaxHealth()) * Player.DEFAULT_HEALTH);
                                g2d.setColor(((player.hasEffect("Poison"))?new Color(39, 161, 18):new Color(209, 21, 33)));
                                g2d.fillRect(12, 12, healthBarWidth, 16);
                                g2d.setColor(Color.WHITE);
                                g2d.drawString(("HP: " + player.getHealth() + "/" + player.getMaxHealth()), 15, 25);
                            }
                        } // End drawing the health bar.
                        { // Draw the experience bar.
                            int expBarWidth = (int)(((double)player.getExp() / (double)player.getNextLevelExp()) * 150);
                            g2d.setColor(new Color(67, 158, 22));
                            g2d.fillRect(12, 34, ((expBarWidth > 150)?150:expBarWidth), 16);
                            g2d.setColor(Color.WHITE);
                            g2d.drawString(("LVL: " + player.getLevel()), 15, 47);
                        } // End drawing the experience bar.
                        // Draw status messages.
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(("Lives: " + player.getLives()), 10, 65);
                        //g2d.drawString(("Level: " + player.getLevel()), 10, 58);
                        g2d.drawString(("Cash: $" + player.getCash()), 10, 78);
                        g2d.drawString(("Ammo: " + player.getWeapon().getAmmoLeft() + "/" + player.getWeapon().getMaxAmmo()),
                                        10, 91);
                        { // Draw Status Icons
                            int x = 5;
                            int y = 101;
                            Iterator<StatusEffect> it = player.getStatusEffects().values().iterator();
                            while(it.hasNext()) {
                                StatusEffect status = it.next();
                                if(status.isActive() && (status.getImage() != null)) {
                                    Composite savedComp = g2d.getComposite();
                                    BufferedImage image = status.getImage();
                                    
                                    // Based on the time until the status effect's expiration, calculate the icon's opacity.
                                    double opacity = 1.0f;
                                    if(Globals.gameTime.getElapsedMillis() >= (status.getEndTime() - 3000))
                                        opacity = ((double)status.getEndTime() - (double)Globals.gameTime.getElapsedMillis()) / 3000;
                                    // Set the composite to use the calculated opacity value.
                                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity));
                                    
                                    // Draw the image using the composite and then restore the composite to its previous state.
                                    g2d.drawImage(image, x, y, null);
                                    g2d.setComposite(savedComp);
                                    
                                    // Increase the X value by the icon width + 5.
                                    x += image.getWidth() + 5;
                                }
                            }
                        } // End Drawing Status Icons
                        framework.getLoadout().draw((Graphics2D)g2d);
                        
                        { // Draw game messages.
                            synchronized(Globals.GAME_MESSAGES) {
                                int i = 0;
                                int x = (int)((Globals.W_WIDTH / 2) - (WeaponsLoadout.BAR_WIDTH / 2));
                                int y = (int)((Globals.W_HEIGHT - (WeaponsLoadout.BAR_HEIGHT + 15)) - 10);
                                g2d.setColor(Color.WHITE);
                                Iterator<Message> it = Globals.GAME_MESSAGES.iterator();
                                while(it.hasNext()) {
                                    if(i < 3) {
                                        Message m = it.next();
                                        if(m.isAlive()) {
                                            m.draw(g2d, new Point2D.Double(x, y));
                                            y -= 12;
                                        }
                                    } else break;
                                }
                            }
                        } // End drawing game messages.

                        g2d.setColor(Color.WHITE);
                        Font font = new Font("Impact", Font.PLAIN, 20);
                        FontMetrics metrics = g2d.getFontMetrics(font);
                        g2d.setFont(font);
                        if(!Globals.waveInProgress) {
                            long timeLeft = Globals.nextWave - Globals.gameTime.getElapsedMillis();
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
                } else if(Globals.storeOpen) {
                    // Draw the store window.
                    this.store.draw(g2d, player);
                } else if(Globals.levelScreenOpen) {
                    // Draw the experience/leveling window.
                    this.levelScreen.draw(g2d, player);
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
