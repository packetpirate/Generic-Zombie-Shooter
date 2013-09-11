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
package genericzombieshooter.structures.components;

import genericzombieshooter.GZSFramework;
import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.structures.weapons.Weapon;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Used to display the store window where the player can buy weapons.
 * @author Darin Beaudreau
 */
public class StoreWindow {
    // Final Variables
    private static final double ITEM_PANE_WIDTH = 256;
    private static final double ITEM_PANE_HEIGHT = 102.4;
    private static final double ITEM_BUTTON_WIDTH = 75;
    private static final double ITEM_BUTTON_HEIGHT = 20;
    
    // Member Variables
    private BufferedImage background;
    private HashMap<String, List<Rectangle2D.Double>> weaponFrames;
    
    public StoreWindow() {
        this.background = GZSFramework.loadImage("/resources/images/GZS_StoreBackground.png");
        this.weaponFrames = new HashMap<String, List<Rectangle2D.Double>>();
        { // Add weapons to icons map.
            {
                Rectangle2D.Double arRect = new Rectangle2D.Double(72, 12.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((arRect.x + arRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((arRect.y + arRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(arRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.ASSAULT_RIFLE.getName(), rects);
            }
            
            {
                Rectangle2D.Double stRect = new Rectangle2D.Double(72, 140.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((stRect.x + stRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((stRect.y + stRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(stRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.SHOTGUN.getName(), rects);
            }
            
            {
                Rectangle2D.Double ftRect = new Rectangle2D.Double(72, 268.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((ftRect.x + ftRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((ftRect.y + ftRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(ftRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.FLAMETHROWER.getName(), rects);
            }
            
            {
                Rectangle2D.Double gnRect = new Rectangle2D.Double(72, 396.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((gnRect.x + gnRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((gnRect.y + gnRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(gnRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.GRENADE.getName(), rects);
            }
            
            {
                Rectangle2D.Double lmRect = new Rectangle2D.Double(72, 524.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((lmRect.x + lmRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((lmRect.y + lmRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(lmRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.LANDMINE.getName(), rects);
            }
            
            {
                Rectangle2D.Double flRect = new Rectangle2D.Double(472, 12.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((flRect.x + flRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((flRect.y + flRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(flRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.FLARE.getName(), rects);
            }
            
            {
                Rectangle2D.Double lwRect = new Rectangle2D.Double(472, 140.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((lwRect.x + lwRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((lwRect.y + lwRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(lwRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.LASERWIRE.getName(), rects);
            }
            
            {
                Rectangle2D.Double twRect = new Rectangle2D.Double(472, 268.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((twRect.x + twRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((twRect.y + twRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(twRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.TURRETWEAPON.getName(), rects);
            }
            
            {
                Rectangle2D.Double tpRect = new Rectangle2D.Double(472, 396.8, 
                        StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT);
                Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((tpRect.x + tpRect.width) - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), 
                                                                      ((tpRect.y + tpRect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                Rectangle2D.Double purchase = new Rectangle2D.Double((buyAmmo.x - (StoreWindow.ITEM_BUTTON_WIDTH + 10)), buyAmmo.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
                List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
                rects.add(tpRect);
                rects.add(purchase);
                rects.add(buyAmmo);
                this.weaponFrames.put(Globals.TELEPORTER.getName(), rects);
            }
        } // End adding weapons to icons map.
    }
    
    public void draw(Graphics2D g2d) {
        if(this.background != null) g2d.drawImage(this.background, 0, 0, null);
        FontMetrics metrics = g2d.getFontMetrics();
        { // Draw Assault Rifle Box
            int x = (int)(this.weaponFrames.get(Globals.ASSAULT_RIFLE.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.ASSAULT_RIFLE.getName()).get(0).y + 17.2);
            BufferedImage rtps = GZSFramework.loadImage("/resources/images/GZS_RTPS.png");
            g2d.drawImage(rtps, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.ASSAULT_RIFLE.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.ASSAULT_RIFLE.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.ASSAULT_RIFLE.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.ASSAULT_RIFLE.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.ASSAULT_RIFLE.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Assault Rifle Box
        { // Draw Shotgun Box
            int x = (int)(this.weaponFrames.get(Globals.SHOTGUN.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.SHOTGUN.getName()).get(0).y + 17.2);
            BufferedImage bmst = GZSFramework.loadImage("/resources/images/GZS_Boomstick.png");
            g2d.drawImage(bmst, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.SHOTGUN.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.SHOTGUN.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.SHOTGUN.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.SHOTGUN.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.SHOTGUN.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Shotgun Box
        { // Draw Flamethrower Box
            int x = (int)(this.weaponFrames.get(Globals.FLAMETHROWER.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.FLAMETHROWER.getName()).get(0).y + 17.2);
            BufferedImage fmth = GZSFramework.loadImage("/resources/images/GZS_Flamethrower.png");
            g2d.drawImage(fmth, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.FLAMETHROWER.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.FLAMETHROWER.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.FLAMETHROWER.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.FLAMETHROWER.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.FLAMETHROWER.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Flamethrower Box
        { // Draw Grenade Box
            int x = (int)(this.weaponFrames.get(Globals.GRENADE.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.GRENADE.getName()).get(0).y + 17.2);
            BufferedImage gren = GZSFramework.loadImage("/resources/images/GZS_Grenade.png");
            g2d.drawImage(gren, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.GRENADE.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.GRENADE.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.GRENADE.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.GRENADE.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.GRENADE.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Grenade Box
        { // Draw Landmine Box
            int x = (int)(this.weaponFrames.get(Globals.LANDMINE.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.LANDMINE.getName()).get(0).y + 17.2);
            BufferedImage lndm = GZSFramework.loadImage("/resources/images/GZS_Landmine.png");
            g2d.drawImage(lndm, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.LANDMINE.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.LANDMINE.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.LANDMINE.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.LANDMINE.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.LANDMINE.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Landmine Box
        { // Draw Flare Box
            int x = (int)(this.weaponFrames.get(Globals.FLARE.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.FLARE.getName()).get(0).y + 17.2);
            BufferedImage flre = GZSFramework.loadImage("/resources/images/GZS_Flare.png");
            g2d.drawImage(flre, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.FLARE.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.FLARE.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.FLARE.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.FLARE.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.FLARE.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Flare Box
        { // Draw Laser Wire Box
            int x = (int)(this.weaponFrames.get(Globals.LASERWIRE.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.LASERWIRE.getName()).get(0).y + 17.2);
            BufferedImage lsrw = GZSFramework.loadImage("/resources/images/GZS_LaserWire.png");
            g2d.drawImage(lsrw, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.LASERWIRE.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.LASERWIRE.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.LASERWIRE.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.LASERWIRE.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.LASERWIRE.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Laser Wire Box
        { // Draw Turret Box
            int x = (int)(this.weaponFrames.get(Globals.TURRETWEAPON.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.TURRETWEAPON.getName()).get(0).y + 17.2);
            BufferedImage turr = GZSFramework.loadImage("/resources/images/GZS_Turret.png");
            g2d.drawImage(turr, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.TURRETWEAPON.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.TURRETWEAPON.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.TURRETWEAPON.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.TURRETWEAPON.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.TURRETWEAPON.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Turret Box
        { // Draw Teleporter Box
            int x = (int)(this.weaponFrames.get(Globals.TELEPORTER.getName()).get(0).x + 18);
            int y = (int)(this.weaponFrames.get(Globals.TELEPORTER.getName()).get(0).y + 17.2);
            BufferedImage brbt = GZSFramework.loadImage("/resources/images/GZS_BigRedButton.png");
            g2d.drawImage(brbt, x, y, null);
            g2d.setColor(Color.WHITE);
            g2d.drawString(Globals.TELEPORTER.getName(), (x + 58), (y + 12));
            g2d.drawString(("Automatic? " + ((Globals.TELEPORTER.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
            g2d.drawString(("Capacity: " + Globals.TELEPORTER.getMaxAmmo()), (x + 58), (y + 40));
            
            String pS = "Purchase";
            Rectangle2D.Double purchase = this.weaponFrames.get(Globals.TELEPORTER.getName()).get(1);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(purchase);
            g2d.setColor(Color.BLACK);
            g2d.draw(purchase);
            g2d.setColor(Color.WHITE);
            g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                                (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
            
            String bS = "Buy Ammo";
            Rectangle2D.Double buyAmmo = this.weaponFrames.get(Globals.TELEPORTER.getName()).get(2);
            g2d.setColor(new Color(8, 84, 22));
            g2d.fill(buyAmmo);
            g2d.setColor(Color.BLACK);
            g2d.draw(buyAmmo);
            g2d.setColor(Color.WHITE);
            g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                               (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        } // End Drawing Teleporter Box
    }
    
    public void click(MouseEvent m, Player player) {
        // Assume click is left mouse since it is checked before it is passed.
        Point2D.Double mousePos = new Point2D.Double(m.getX(), m.getY());
        
        // Check all weapon frames and check the purchase and buy ammo buttons.
        Iterator it = this.weaponFrames.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, List<Rectangle2D.Double>> pair = (Map.Entry<String, List<Rectangle2D.Double>>)it.next();
            if(pair.getValue().get(1).contains(mousePos)) {
                // Check if player already has the weapon.
                if(!player.hasWeapon(pair.getKey())) {
                    // If not, check if the player has enough cash to buy it.
                    Weapon w = Globals.getWeaponByName(pair.getKey());
                    if(w != null) {
                        if(player.getCash() >= w.getWeaponPrice()) {
                            /* Deduct the cash amount from the player's total
                               and add the weapon to the player's inventory. */
                            player.removeCash(w.getWeaponPrice());
                            player.addWeapon(w);
                        }
                    }
                }
                break;
            } else if(pair.getValue().get(2).contains(mousePos)) {
                // Check if player has enough money to purchase the weapon's ammo.
                if(player.hasWeapon(pair.getKey())) {
                    // If so, check if the player has enough money to buy the ammo.
                    Weapon w = Globals.getWeaponByName(pair.getKey());
                    if(w != null) {
                        // If the selected weapon's ammo stock is not already full...
                        if(w.getAmmoLeft() < w.getMaxAmmo()) {
                            if(player.getCash() >= w.getAmmoPrice()) {
                                /* Deduct the cash amount from the player's total
                                   and add the ammo amount to the player's stock. */
                                player.removeCash(w.getAmmoPrice());
                                w.addAmmo(w.getAmmoPackAmount());
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
