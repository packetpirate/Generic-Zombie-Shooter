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
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.Sounds;
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
    private static final double ITEM_BUTTON_WIDTH = 115;
    private static final double ITEM_BUTTON_HEIGHT = 20;
    
    // Member Variables
    private BufferedImage background;
    private HashMap<String, List<Rectangle2D.Double>> weaponFrames;
    
    public StoreWindow() {
        this.background = GZSFramework.loadImage("/resources/images/GZS_StoreBackground.png");
        this.weaponFrames = new HashMap<String, List<Rectangle2D.Double>>();
        { // Add weapons to icons map.
            this.createWeaponFrame(Globals.ASSAULT_RIFLE, 
                                   new Rectangle2D.Double(72, 12.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.SHOTGUN, 
                                   new Rectangle2D.Double(72, 140.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.FLAMETHROWER, 
                                   new Rectangle2D.Double(72, 268.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.GRENADE, 
                                   new Rectangle2D.Double(72, 396.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.LANDMINE, 
                                   new Rectangle2D.Double(72, 524.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.FLARE, 
                                   new Rectangle2D.Double(472, 12.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.LASERWIRE, 
                                   new Rectangle2D.Double(472, 140.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.TURRETWEAPON, 
                                   new Rectangle2D.Double(472, 268.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
            this.createWeaponFrame(Globals.TELEPORTER, 
                                   new Rectangle2D.Double(472, 396.8, 
                    StoreWindow.ITEM_PANE_WIDTH, StoreWindow.ITEM_PANE_HEIGHT));
        } // End adding weapons to icons map.
    }
    
    private void createWeaponFrame(Weapon w, Rectangle2D.Double rect) {
        Rectangle2D.Double purchase = new Rectangle2D.Double((rect.x + 10), 
                        ((rect.y + rect.height) - (StoreWindow.ITEM_BUTTON_HEIGHT + 10)), 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
        Rectangle2D.Double buyAmmo = new Rectangle2D.Double(((purchase.x + purchase.width) + 6), purchase.y, 
                        StoreWindow.ITEM_BUTTON_WIDTH, StoreWindow.ITEM_BUTTON_HEIGHT);
        List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
        rects.add(rect);
        rects.add(purchase);
        rects.add(buyAmmo);
        this.weaponFrames.put(w.getName(), rects);
    }
    
    public void draw(Graphics2D g2d, Player player) {
        if(this.background != null) g2d.drawImage(this.background, 0, 0, null);
        this.drawWeaponFrame(g2d, player, Globals.ASSAULT_RIFLE, Globals.ASSAULT_RIFLE.getImage());
        this.drawWeaponFrame(g2d, player, Globals.SHOTGUN, Globals.SHOTGUN.getImage());
        this.drawWeaponFrame(g2d, player, Globals.FLAMETHROWER, Globals.FLAMETHROWER.getImage());
        this.drawWeaponFrame(g2d, player, Globals.GRENADE, Globals.GRENADE.getImage());
        this.drawWeaponFrame(g2d, player, Globals.LANDMINE, Globals.LANDMINE.getImage());
        this.drawWeaponFrame(g2d, player, Globals.FLARE, Globals.FLARE.getImage());
        this.drawWeaponFrame(g2d, player, Globals.LASERWIRE, Globals.LASERWIRE.getImage());
        this.drawWeaponFrame(g2d, player, Globals.TURRETWEAPON, Globals.TURRETWEAPON.getImage());
        this.drawWeaponFrame(g2d, player, Globals.TELEPORTER, Globals.TELEPORTER.getImage());
    }
    
    private void drawWeaponFrame(Graphics2D g2d, Player player, Weapon w, BufferedImage brbt) {
        FontMetrics metrics = g2d.getFontMetrics();
        
        int x = (int)(this.weaponFrames.get(w.getName()).get(0).x + 18);
        int y = (int)(this.weaponFrames.get(w.getName()).get(0).y + 17.2);
        g2d.drawImage(brbt, x, y, null);
        g2d.setColor(Color.WHITE);
        g2d.drawString(w.getName(), (x + 58), (y + 12));
        g2d.drawString(("Automatic? " + ((w.isAutomatic())?"Yes":"No")), (x + 58), (y + 26));
        g2d.drawString(("Capacity: " + w.getMaxAmmo()), (x + 58), (y + 40));
        
        String pS = "Purchase : $" + w.getWeaponPrice();
        Rectangle2D.Double purchase = this.weaponFrames.get(w.getName()).get(1);
        if(!player.hasWeapon(w.getName())) g2d.setColor(new Color(8, 84, 22));
        else g2d.setColor(Color.DARK_GRAY);
        g2d.fill(purchase);
        g2d.setColor(Color.BLACK);
        g2d.draw(purchase);
        g2d.setColor(Color.WHITE);
        g2d.drawString(pS, (int)((purchase.x + (purchase.width / 2)) - (metrics.stringWidth(pS) / 2)), 
                            (int)((purchase.y + (purchase.height / 2)) + ((metrics.getHeight() / 2) - 3)));
        
        String bS = "Buy Ammo: $" + w.getAmmoPrice();
        Rectangle2D.Double buyAmmo = this.weaponFrames.get(w.getName()).get(2);
        if(player.hasWeapon(w.getName()) && !w.ammoFull()) g2d.setColor(new Color(8, 84, 22));
        else g2d.setColor(Color.DARK_GRAY);
        g2d.fill(buyAmmo);
        g2d.setColor(Color.BLACK);
        g2d.draw(buyAmmo);
        g2d.setColor(Color.WHITE);
        g2d.drawString(bS, (int)((buyAmmo.x + (buyAmmo.width / 2)) - (metrics.stringWidth(bS) / 2)), 
                           (int)((buyAmmo.y + (buyAmmo.height / 2)) + ((metrics.getHeight() / 2) - 3)));
    }
    
    public void click(MouseEvent m, Player player) {
        // Assume click is left mouse since it is checked before it is passed.
        Point2D.Double mousePos = new Point2D.Double(m.getX(), m.getY());
        // Offset the mouse position by half the width and height of the mouse cursor image.
        mousePos.x += Images.CROSSHAIR.getWidth() / 2;
        mousePos.y += Images.CROSSHAIR.getHeight() / 2;
        
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
                            Sounds.PURCHASEWEAPON.play();
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
                        if(!w.ammoFull()) {
                            if(player.getCash() >= w.getAmmoPrice()) {
                                /* Deduct the cash amount from the player's total
                                   and add the ammo amount to the player's stock. */
                                player.removeCash(w.getAmmoPrice());
                                w.addAmmo(w.getAmmoPackAmount());
                                Sounds.BUYAMMO.play();
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
